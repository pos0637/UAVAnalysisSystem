package org.jeecg.modules.uav.miscs;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.uav.vo.UavPath;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * LAS工具类
 *
 * @author Alex
 */
public final class LasUtils {
    private static void putUnsignedByte(ByteBuffer bb, int value) {
        bb.put((byte) (value & 0xff));
    }

    private static void putUnsignedShort(ByteBuffer bb, int value) {
        bb.putShort((short) (value & 0xffff));
    }

    private static void putUnsignedInt(ByteBuffer bb, long value) {
        bb.putInt((int) (value & 0xffffffffL));
    }

    /**
     * 序列化
     *
     * @param object 对象
     * @return 字节数组
     */
    private static byte[] lasSerialize(final Object object) throws IllegalAccessException {
        final List<Byte> list = new ArrayList<>();
        final Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            ByteBuffer bb = null;
            if (field.getType().equals(byte.class)) {
                bb = ByteBuffer.wrap(new byte[1]).order(ByteOrder.LITTLE_ENDIAN);
                putUnsignedByte(bb, field.getByte(object));
            } else if (field.getType().equals(short.class)) {
                bb = ByteBuffer.wrap(new byte[2]).order(ByteOrder.LITTLE_ENDIAN);
                putUnsignedShort(bb, field.getShort(object));
            } else if (field.getType().equals(int.class)) {
                bb = ByteBuffer.wrap(new byte[4]).order(ByteOrder.LITTLE_ENDIAN);
                putUnsignedInt(bb, field.getInt(object));
            } else if (field.getType().equals(double.class)) {
                bb = ByteBuffer.wrap(new byte[8]).order(ByteOrder.LITTLE_ENDIAN);
                bb.putLong(Double.doubleToLongBits(field.getDouble(object)));
            } else if (field.getType().equals(byte[].class)) {
                final byte[] byteArray = (byte[]) field.get(object);
                if (byteArray != null) {
                    bb = ByteBuffer.wrap(new byte[byteArray.length]).order(ByteOrder.LITTLE_ENDIAN);
                    for (byte b : byteArray) {
                        putUnsignedByte(bb, b);
                    }
                }
            } else if (field.getType().equals(short[].class)) {
                final short[] shortArray = (short[]) field.get(object);
                if (shortArray != null) {
                    bb = ByteBuffer.wrap(new byte[2 * shortArray.length]).order(ByteOrder.LITTLE_ENDIAN);
                    for (short s : shortArray) {
                        bb.putShort(s);
                    }
                }
            } else if (field.getType().equals(int[].class)) {
                final int[] intArray = (int[]) field.get(object);
                if (intArray != null) {
                    bb = ByteBuffer.wrap(new byte[4 * intArray.length]).order(ByteOrder.LITTLE_ENDIAN);
                    for (int i : intArray) {
                        bb.putInt(i);
                    }
                }
            }

            if (bb != null) {
                bb.flip();
                final byte[] array = new byte[bb.capacity()];
                bb.get(array);
                for (byte b : array) {
                    list.add(b);
                }
            }
        }

        return Bytes.toArray(list);
    }

    /**
     * 生成LAS文件
     *
     * @param bos    输出数据流
     * @param points 偏移轨迹点集合
     * @throws IOException            异常
     * @throws IllegalAccessException 异常
     */
    public static void generateLasFile(final BufferedOutputStream bos, final List<UavPath.OffsetPoint> points) throws IOException, IllegalAccessException {
        // generate las file header
        final LasHeader header = new LasHeader();
        header.numberOfPointRecords = points.size();

        double xMax = Double.MIN_VALUE, yMax = Double.MIN_VALUE, zMax = Double.MIN_VALUE;
        double xMin = Double.MAX_VALUE, yMin = Double.MAX_VALUE, zMin = Double.MAX_VALUE;
        final double x_scale = header.x_scale;
        final double y_scale = header.y_scale;
        final double z_scale = header.z_scale;

        // generate las file body
        for (UavPath.OffsetPoint point : points) {
            if (xMax < point.getX()) {
                xMax = point.getX();
            }

            if (xMin > point.getX()) {
                xMin = point.getX();
            }

            if (yMax < point.getY()) {
                yMax = point.getY();
            }

            if (yMin > point.getY()) {
                yMin = point.getY();
            }

            if (zMax < point.getZ()) {
                zMax = point.getZ();
            }

            if (zMin > point.getZ()) {
                zMin = point.getZ();
            }
        }

        header.x_max = xMax;
        header.x_offset = header.x_min = xMin;
        header.y_max = yMax;
        header.y_offset = header.y_min = yMin;
        header.z_max = zMax;
        header.z_offset = header.z_min = zMin;

        // write las file header
        bos.write(lasSerialize(header));

        // write las file body
        for (UavPath.OffsetPoint point : points) {
            final LasPoint p = new LasPoint();
            p.intensity = 0;
            p.returnNumber = 0;
            p.classification = 0;
            p.scanAngleRank = 0;
            p.userData = 0;
            p.pointSourceID = 0;

            p.x = (int) ((point.getX() - xMin) / x_scale);
            p.y = (int) ((point.getY() - yMin) / y_scale);
            p.z = (int) ((point.getZ() - zMin) / z_scale);
            p.time = point.getTime();

            bos.write(lasSerialize(point));
        }
    }

    /**
     * LAS文件头
     */
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
         * 点数据数目
         */
        public int numberOfPointRecords;
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

    /**
     * LAS轨迹点
     */
    @Getter
    @Setter
    private final static class LasPoint {
        public int x;
        public int y;
        public int z;
        public short intensity;
        public byte returnNumber;
        public byte classification;
        public byte scanAngleRank;
        public byte userData;
        public short pointSourceID;
        /**
         * GPS Time
         */
        public double time;
    }
}
