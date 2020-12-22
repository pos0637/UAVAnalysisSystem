package org.jeecg.modules.uav.miscs;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.uav.vo.UavPath;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * NMEA工具
 *
 * @author Alex
 */
@Slf4j
public final class NMEAUtils {
    /**
     * GPS定位数据
     */
    private static final String GPGGA = "$GPGGA";

    /**
     * 地球赤道半径(公里)
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 日期格式
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hhmmss.SSS");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * 角度转弧度
     *
     * @param degree 角度
     * @return 弧度
     */
    private static double deg2Rad(final double degree) {
        return degree * Math.PI / 180.0;
    }

    /**
     * 计算经纬度距离
     *
     * @param lng1 经度
     * @param lat1 纬度
     * @param lng2 经度
     * @param lat2 纬度
     * @return 距离(单位 : 米)
     */
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
     * 获取轨迹点集合
     *
     * @param path 文件路径
     * @return 轨迹点集合
     */
    public static List<UavPath.Point> getPoints(final String path) {
        try {
            final List<UavPath.Point> list = new LinkedList<>();
            final CsvReader reader = CsvUtil.getReader();
            final CsvData data = reader.read(FileUtil.file(path));
            final List<CsvRow> rows = data.getRows();

            for (CsvRow csvRow : rows) {
                List<String> cols = csvRow.getRawList();
                if (GPGGA.equals(cols.get(0))) {
                    // GPS定位信息
                    list.add(new UavPath.Point(
                            DATE_FORMAT.parse("1970-01-01 " + cols.get(1)),
                            Convert.toFloat(cols.get(4)),
                            Convert.toFloat(cols.get(2)),
                            Convert.toFloat(cols.get(9))
                    ));
                }
            }

            return list;
        } catch (Exception e) {
            log.error(null, e);
            return null;
        }
    }

    /**
     * 获取偏移轨迹点集合
     *
     * @param path            文件路径
     * @param centerLongitude 原点经度
     * @param centerLatitude  原点纬度
     * @param centerAltitude  原点高程
     * @return 偏移轨迹点集合
     */
    public static List<UavPath.OffsetPoint> getOffsetPoints(final String path, final Double centerLongitude, final Double centerLatitude, final Double centerAltitude) {
        final List<UavPath.Point> list = getPoints(path);
        if (list == null) {
            return null;
        }

        if (list.size() == 0) {
            return new ArrayList<>();
        }

        // 计算原点坐标
        final long time = list.get(0).getTime().getTime();
        final double centerLongitude1 = centerLongitude != null ? centerLongitude : list.get(0).getLng();
        final double centerLatitude1 = centerLatitude != null ? centerLatitude : list.get(0).getLat();
        final double centerAltitude1 = centerAltitude != null ? centerAltitude : list.get(0).getAlt();

        return list.stream().map(p -> new UavPath.OffsetPoint(
                p.getTime().getTime() - time,
                getDistance(p.getLng() / 100.0, 0.0, centerLongitude1 / 100.0, 0.0),
                getDistance(0, p.getLat() / 100.0, 0.0, centerLatitude1 / 100.0),
                p.getAlt() - centerAltitude1
        )).collect(Collectors.toList());
    }
}
