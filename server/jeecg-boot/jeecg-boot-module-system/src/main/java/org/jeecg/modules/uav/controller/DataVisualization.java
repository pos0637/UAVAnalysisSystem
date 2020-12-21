package org.jeecg.modules.uav.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.google.common.primitives.Bytes;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.uav.entity.UavPath;
import org.jeecg.modules.uav.service.IUavPathService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * 数据可视化控制器
 *
 * @author Alex
 */
@RestController
@RequestMapping("/uav/datav")
@Slf4j
public final class DataVisualization {
    /**
     * 地球赤道半径(公里)
     */
    private static final double EARTH_RADIUS = 6378.137;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hhmmss.sss");

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Autowired
    private IUavPathService uavPathService;
    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    private static double deg2Rad(final double degree) {
        return degree * Math.PI / 180.0;
    }

    private static double getDistance(final double lng1, final double lat1, final double lng2, final double lat2) {
        final double rad1 = deg2Rad(lat1);
        final double rad2 = deg2Rad(lat2);
        final double a = rad1 - rad2;
        final double b = deg2Rad(lng1) - deg2Rad(lng2);

        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000.0) / 10000.0;

        return distance * 1000;
    }

    /**
     * 序列化
     *
     * @param object 对象
     * @return 字节数组
     */
    private static byte[] lasSerialize(final Object object) {
        try {
            final List<Byte> list = new ArrayList<>();
            final Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                byte[] array = null;
                if (field.getType().equals(Byte.class)) {
                    array = new byte[1];
                    array[0] = field.getByte(object);
                } else if (field.getType().equals(short.class)) {
                    array = new byte[2];
                    ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).putShort(field.getShort(object));
                } else if (field.getType().equals(int.class)) {
                    array = new byte[4];
                    ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).putInt(field.getInt(object));
                } else if (field.getType().equals(double.class)) {
                    array = new byte[8];
                    ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).putDouble(field.getDouble(object));
                } else if (field.getType().equals(byte[].class)) {
                    array = (byte[]) field.get(object);
                } else if (field.getType().equals(short[].class)) {
                    final short[] shortArray = (short[]) field.get(object);
                    if (shortArray != null) {
                        array = new byte[2 * shortArray.length];
                        final ByteBuffer bb = ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN);
                        for (short s : shortArray) {
                            bb.putShort(s);
                        }
                    }
                } else if (field.getType().equals(int[].class)) {
                    final int[] intArray = (int[]) field.get(object);
                    if (intArray != null) {
                        array = new byte[4 * intArray.length];
                        final ByteBuffer bb = ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN);
                        for (int i : intArray) {
                            bb.putInt(i);
                        }
                    }
                }

                if (array != null) {
                    for (byte b : array) {
                        list.add(b);
                    }
                }
            }

            return Bytes.toArray(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * UTC时间转毫秒
     *
     * @param utc UTC时间
     * @return 毫秒
     * @throws ParseException 异常
     */
    private static long getMilliseconds(final String utc) throws ParseException {
        return sdf.parse("1970-01-01 " + utc).getTime();
    }

    @GetMapping(value = "/paths")
    public final Result<?> getPaths() {
        return Result.ok(uavPathService.list());
    }

    @GetMapping(value = "/path")
    public final Result<?> getPath(@RequestParam(name = "id", required = true) String id) {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return Result.error("未找到对应数据");
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return Result.error("未找到对应数据");
        }

        final List<org.jeecg.modules.uav.vo.UavPath.Point> list = new LinkedList<>();
        final CsvReader reader = CsvUtil.getReader();
        final CsvData data = reader.read(FileUtil.file(filePath));
        final List<CsvRow> rows = data.getRows();
        Double centerLongitude = uavPath.getCenterLongitude();
        Double centerLatitude = uavPath.getCenterLatitude();
        Double centerAltitude = uavPath.getCenterAltitude();

        for (CsvRow csvRow : rows) {
            List<String> cols = csvRow.getRawList();
            if (cols.get(0).equals("$GPGGA")) {
                // GPS定位信息
                if (centerLongitude == null) {
                    centerLongitude = Convert.toDouble(cols.get(4));
                }
                if (centerLatitude == null) {
                    centerLatitude = Convert.toDouble(cols.get(2));
                }
                if (centerAltitude == null) {
                    centerAltitude = Convert.toDouble(cols.get(9));
                }

                list.add(new org.jeecg.modules.uav.vo.UavPath.Point(
                        DateUtil.parse(cols.get(1), "hhmmss.SSS"),
                        Convert.toFloat(cols.get(4)),
                        Convert.toFloat(cols.get(2)),
                        Convert.toFloat(cols.get(9))
                ));
            }
        }

        final org.jeecg.modules.uav.vo.UavPath path = new org.jeecg.modules.uav.vo.UavPath();
        path.setId(uavPath.getId());
        path.setName(uavPath.getName());
        path.setDescription(uavPath.getDescription());
        path.setCenterLongitude(centerLongitude);
        path.setCenterLatitude(centerLatitude);
        path.setCenterAltitude(centerAltitude);
        path.setPoints(list);

        return Result.ok(path);
    }

    @RequestMapping(value = "/exportXls")
    public final ModelAndView exportXls(@RequestParam(name = "id", required = true) String id) {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return null;
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        final List<org.jeecg.modules.uav.vo.UavPath.Point> list = new ArrayList<>();
        final CsvReader reader = CsvUtil.getReader();
        final CsvData data = reader.read(FileUtil.file(filePath));
        final List<CsvRow> rows = data.getRows();
        for (CsvRow csvRow : rows) {
            List<String> cols = csvRow.getRawList();
            if (cols.get(0).equals("$GPGGA")) {
                // GPS定位信息
                list.add(new org.jeecg.modules.uav.vo.UavPath.Point(
                        DateUtil.parse(cols.get(1), "hhmmss.SSS"),
                        Convert.toFloat(cols.get(4)),
                        Convert.toFloat(cols.get(2)),
                        Convert.toFloat(cols.get(9))
                ));
            }
        }

        final ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, uavPath.getName());
        mv.addObject(NormalExcelConstants.CLASS, org.jeecg.modules.uav.vo.UavPath.Point.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("无人机轨迹数据", uavPath.getName()));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    @RequestMapping(value = "/exportPointCloud")
    public final void exportPointCloud(@RequestParam(name = "id", required = true) String id, final HttpServletResponse response) throws Exception {
        final UavPath uavPath = uavPathService.getById(id);
        if (uavPath == null) {
            return;
        }

        final String filePath = uploadpath + File.separator + uavPath.getFile();
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        final String fileName = "无人机轨迹数据_" + uavPath.getName() + ".las";
        response.setHeader("Content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + "\";charset=UTF-8");

        final CsvReader reader = CsvUtil.getReader();
        final CsvData data = reader.read(FileUtil.file(filePath));
        final List<CsvRow> rows = data.getRows();

        try (final BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            // generate las file header
            final LasHeader header = new LasHeader();
            header.numberOfPointRecords = rows.size();

            double xMax = Double.MIN_VALUE, yMax = Double.MIN_VALUE, zMax = Double.MIN_VALUE;
            double xMin = Double.MAX_VALUE, yMin = Double.MAX_VALUE, zMin = Double.MAX_VALUE;
            final double x_scale = header.x_scale;
            final double y_scale = header.y_scale;
            final double z_scale = header.z_scale;
            Double centerLongitude = uavPath.getCenterLongitude();
            Double centerLatitude = uavPath.getCenterLatitude();
            Double centerAltitude = uavPath.getCenterAltitude();
            long baseTime = 0;

            // generate las file body
            for (CsvRow csvRow : rows) {
                List<String> cols = csvRow.getRawList();
                if (cols.get(0).equals("$GPGGA")) {
                    // GPS定位信息
                    if (centerLongitude == null) {
                        centerLongitude = Convert.toDouble(cols.get(4));
                    }
                    if (centerLatitude == null) {
                        centerLatitude = Convert.toDouble(cols.get(2));
                    }
                    if (centerAltitude == null) {
                        centerAltitude = Convert.toDouble(cols.get(9));
                    }
                    if (baseTime == 0) {
                        baseTime = getMilliseconds(cols.get(1));
                    }

                    final double x = getDistance(Convert.toFloat(cols.get(4)) / 100.0, 0.0, centerLongitude / 100.0, 0.0);
                    final double y = getDistance(0, Convert.toFloat(cols.get(2)) / 100.0, 0.0, centerLatitude / 100.0);
                    final double z = Convert.toFloat(cols.get(9)) - centerAltitude;

                    if (xMax < x) {
                        xMax = x;
                    }

                    if (xMin > x) {
                        xMin = x;
                    }

                    if (yMax < y) {
                        yMax = y;
                    }

                    if (yMin > y) {
                        yMin = y;
                    }

                    if (zMax < z) {
                        zMax = z;
                    }

                    if (zMin > z) {
                        zMin = z;
                    }
                }
            }
            header.x_max = xMax;
            header.x_offset = header.x_min = xMin;
            header.y_max = yMax;
            header.y_offset = header.y_min = yMin;
            header.z_max = zMax;
            header.z_offset = header.z_min = zMin;

            // generate las file body
            final List<LasPoint> points = new ArrayList<>();
            for (CsvRow csvRow : rows) {
                List<String> cols = csvRow.getRawList();
                if (cols.get(0).equals("$GPGGA")) {
                    // GPS定位信息
                    final double x = getDistance(Convert.toFloat(cols.get(4)) / 100.0, 0.0, centerLongitude / 100.0, 0.0);
                    final double y = getDistance(0, Convert.toFloat(cols.get(2)) / 100.0, 0.0, centerLatitude / 100.0);
                    final double z = Convert.toFloat(cols.get(9)) - centerAltitude;
                    final long time = getMilliseconds(cols.get(1)) - baseTime;

                    final LasPoint point = new LasPoint();
                    point.intensity = 0;
                    point.returnNumber = 0;
                    point.classification = 0;
                    point.scanAngleRank = 0;
                    point.userData = 0;
                    point.pointSourceID = 0;

                    point.x = (int) ((x - xMin) / x_scale);
                    point.y = (int) ((y - yMin) / y_scale);
                    point.z = (int) ((z - zMin) / z_scale);
                    point.time = time;

                    points.add(point);
                }
            }

            // write las file header
            bos.write(lasSerialize(header));

            // write las file body
            for (LasPoint point : points) {
                bos.write(lasSerialize(point));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        response.getWriter().flush();
        response.getWriter().close();
    }

    @Getter
    @Setter
    private final static class LasHeader {
        /**
         * 文件标识（“LASF”）
         */
        public final byte[] file_signature;

        /**
         * 文件ID
         */
        public final short file_source_id;

        /**
         * 全球编码 设置GPS时间格式 分别为0和1
         */
        public final short global_encoding;

        /**
         * GUID数据
         */
        public final int project_id_guid_data1;

        public final short project_id_guid_data2;

        public final short project_id_guid_data3;

        /**
         * 数组长度为8
         */
        public final byte[] project_id_guid_data4;

        /**
         * 版本信息
         */
        public final byte version_major;

        public final byte version_minor;

        /**
         * 系统标识数组长度为32
         */
        public final byte[] systemID;

        /**
         * 文件创建时间 数组长度为32
         */
        public final byte[] generatingSoftware;
        public final short fileCreationDay;

        public final short fileCreationYear;

        /**
         * 头部大小
         */
        public final short headerSize;

        /**
         * 数据偏移
         */
        public final int pointDataOffset;

        /**
         * 变长记录域数目
         */
        public final int numberOfVariableRecords;

        /**
         * 点数据格式
         */
        public final byte pointDataFormatID;

        /**
         * 点数据长度
         */
        public final short pointDataRecordLength;

        /**
         * 不同回波点数目 数组长度为5
         */
        public final int[] numberOfPointsByReturn;

        /**
         * X、Y、Z刻度因子
         */
        public final double x_scale;
        public final double y_scale;
        public final double z_scale;

        /**
         * X、Y、Z偏移值
         */
        public double x_offset;
        public double y_offset;
        public double z_offset;

        /**
         * X、Y、Z最大最小值
         */
        public double x_max;
        public double x_min;
        public double y_max;
        public double y_min;
        public double z_max;
        public double z_min;

        /**
         * 点数据数目
         */
        public int numberOfPointRecords;

        public LasHeader() {
            file_signature = new byte[]{(byte) 'L', (byte) 'A', (byte) 'S', (byte) 'F'};
            file_source_id = 0;
            global_encoding = 0;

            project_id_guid_data1 = 0;
            project_id_guid_data2 = 0;
            project_id_guid_data3 = 0;
            project_id_guid_data4 = new byte[8];

            version_major = 1;
            version_minor = 2;

            systemID = new byte[32];
            systemID[0] = (byte) 'G';
            systemID[1] = (byte) 'H';
            systemID[2] = (byte) 'Z';
            systemID[3] = (byte) 'N';
            generatingSoftware = new byte[32];
            fileCreationDay = 0;
            fileCreationYear = 0;
            headerSize = 227;
            pointDataOffset = 227;
            numberOfVariableRecords = 0;
            pointDataFormatID = 1;
            pointDataRecordLength = 28;
            numberOfPointRecords = 0;
            numberOfPointsByReturn = new int[5];

            x_scale = 0.0001;
            y_scale = 0.0001;
            z_scale = 0.0001;

            x_offset = 0;
            y_offset = 0;
            z_offset = 0;

            x_max = 0;
            x_min = 0;
            y_max = 0;
            y_min = 0;
            z_max = 0;
            z_min = 0;
        }
    }

    @Getter
    @Setter
    private final static class LasPoint {
        /**
         * GPS Time
         */
        public double time;
        public int x;
        public int y;
        public int z;
        public short intensity;
        public byte returnNumber;
        public byte classification;
        public byte scanAngleRank;
        public byte userData;
        public short pointSourceID;
    }
}
