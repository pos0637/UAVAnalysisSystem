package org.jeecg.modules.uav.miscs;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.uav.vo.UavPath;

import java.text.ParseException;
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

    private static double transformLat(final double x, final double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += ((20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0) / 3.0;
        ret += ((20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin((y / 3.0) * Math.PI)) * 2.0) / 3.0;
        ret += ((160.0 * Math.sin((y / 12.0) * Math.PI) + 320 * Math.sin((y * Math.PI) / 30.0)) * 2.0) / 3.0;
        return ret;
    }

    private static double transformLng(final double x, final double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += ((20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0) / 3.0;
        ret += ((20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin((x / 3.0) * Math.PI)) * 2.0) / 3.0;
        ret += ((150.0 * Math.sin((x / 12.0) * Math.PI) + 300.0 * Math.sin((x / 30.0) * Math.PI)) * 2.0) / 3.0;
        return ret;
    }

    private static UavPath.Point delta(final UavPath.Point point) {
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        final double a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        final double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        double lat = transformLat(point.getLng() - 105.0, point.getLat() - 35.0);
        double lng = transformLng(point.getLng() - 105.0, point.getLat() - 35.0);
        final double radLat = (point.getLat() / 180.0) * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;

        final double sqrtMagic = Math.sqrt(magic);
        lat = (lat * 180.0) / (((a * (1 - ee)) / (magic * sqrtMagic)) * Math.PI);
        lng = (lng * 180.0) / ((a / sqrtMagic) * Math.cos(radLat) * Math.PI);

        return new UavPath.Point(point.getTime(), point.getLng() + lng, point.getLat() + lat, point.getAlt());
    }

    private static boolean outOfChina(final UavPath.Point point) {
        return (point.getLng() < 72.004 || point.getLng() > 137.8347 || point.getLat() < 0.8293 || point.getLat() > 55.8271);
    }

    /**
     * WGS-84坐标转GCJ-02坐标
     *
     * @param point WGS-84坐标点
     * @return GCJ-02坐标点
     */
    public static UavPath.Point gcjEncrypt(final UavPath.Point point) {
        return outOfChina(point) ? point : delta(point);
    }

    /**
     * 计算经纬度 ddmm.mmmmmmm -> dd.ddddd
     *
     * @param lngLat 经纬度
     * @return 经纬度
     */
    private static double getLngLat(final String lngLat) {
        final int dot = lngLat.indexOf('.');
        final String d = lngLat.substring(0, dot - 2);
        final String m = lngLat.substring(dot - 2);
        return Convert.toDouble(d) + (Convert.toDouble(m) / 60.0);
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
                    list.add(gcjEncrypt(new UavPath.Point(
                            cols.get(1),
                            getLngLat(cols.get(4)),
                            getLngLat(cols.get(2)),
                            Convert.toFloat(cols.get(9))
                    )));
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
    public static List<UavPath.OffsetPoint> getOffsetPoints(final String path, final Double centerLongitude, final Double centerLatitude, final Double centerAltitude) throws ParseException {
        final List<UavPath.Point> list = getPoints(path);
        if (list == null) {
            return null;
        }

        if (list.size() == 0) {
            return new ArrayList<>();
        }

        // 计算原点坐标
        final long time = DATE_FORMAT.parse("1970-01-01 " + list.get(0).getTime()).getTime();
        final double centerLongitude1 = centerLongitude != null ? centerLongitude : list.get(0).getLng();
        final double centerLatitude1 = centerLatitude != null ? centerLatitude : list.get(0).getLat();
        final double centerAltitude1 = centerAltitude != null ? centerAltitude : list.get(0).getAlt();

        return list.stream().map(p -> {
            try {
                return new UavPath.OffsetPoint(
                        DATE_FORMAT.parse("1970-01-01 " + p.getTime()).getTime() - time,
                        getDistance(p.getLng(), 0.0, centerLongitude1, 0.0) * (p.getLng() > centerLongitude1 ? 1.0 : -1.0),
                        getDistance(0, p.getLat(), 0.0, centerLatitude1) * (p.getLat() > centerLatitude1 ? 1.0 : -1.0),
                        p.getAlt() - centerAltitude1
                );
            } catch (ParseException e) {
                log.error(null, e);
                return null;
            }
        }).collect(Collectors.toList());
    }
}
