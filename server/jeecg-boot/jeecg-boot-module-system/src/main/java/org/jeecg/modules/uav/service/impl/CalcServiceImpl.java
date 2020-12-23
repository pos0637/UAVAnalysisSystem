package org.jeecg.modules.uav.service.impl;

import org.jeecg.modules.uav.miscs.NMEAUtils;
import org.jeecg.modules.uav.service.ICalcService;
import org.jeecg.modules.uav.vo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 数据计算业务层
 *
 * @author liujianning
 */
@Service
public class CalcServiceImpl implements ICalcService {

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    /**
     * 最大作业半径
     *
     * @param params 计算参数
     * @return
     */
    @Override
    public Double getMaxRadius(RadiusParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint c1 = list1.get(params.getC1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint c2 = list2.get(params.getC2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());
        UavPath.OffsetPoint c3 = list3.get(params.getC3());

        Double r1 = (spaceDistance(a1, b1) + spaceDistance(b1, c1)) / 2;
        Double r2 = (spaceDistance(a2, b2) + spaceDistance(b2, c2)) / 2;
        Double r3 = (spaceDistance(a3, b3) + spaceDistance(b3, c3)) / 2;
        Double min = r1 < r2 ? r1 : r2;
        return min < r3 ? min : r3;
    }

    /**
     * 最大平飞速度
     *
     * @param params
     * @return
     */
    @Override
    public Double getMaxPFS(PFSParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);
        List<UavPath.OffsetPoint> list4 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile4(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());
        UavPath.OffsetPoint a4 = list4.get(params.getA4());
        UavPath.OffsetPoint b4 = list4.get(params.getB4());

        Double s1 = spaceDistance(a1, b1) / ((b1.getTime() - a1.getTime()) / 1000);
        Double s2 = spaceDistance(a2, b2) / ((b2.getTime() - a2.getTime()) / 1000);
        Double s3 = spaceDistance(a3, b3) / ((b3.getTime() - a3.getTime()) / 1000);
        Double s4 = spaceDistance(a4, b4) / ((b4.getTime() - a4.getTime()) / 1000);

        Double avg = (s1 + s2 + s3 + s4) / 4;

        return Double.isInfinite(avg) || Double.isNaN(avg) ? 0d : avg;
    }

    /**
     * 最大爬升速率
     *
     * @param params
     * @return
     */
    @Override
    public Double getMaxCS(CSParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());

        Double s1 = spaceDistance(a1, b1) / Math.abs((b1.getTime() - a1.getTime()) / 1000);
        Double s2 = spaceDistance(a2, b2) / Math.abs((b2.getTime() - a2.getTime()) / 1000);
        Double s3 = spaceDistance(a3, b3) / Math.abs((b3.getTime() - a3.getTime()) / 1000);

        Double min = s1 < s2 ? s1 : s2;
        min = min < s3 ? min : s3;

        return Double.isInfinite(min) || Double.isNaN(min) ? 0d : min;
    }

    /**
     * 高度保持性能
     *
     * @param params
     * @return
     */
    @Override
    public Double getHRP(HRPParams params) {
        List<UavPath.OffsetPoint> list = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);

        // 第一阶段点位集
        List<UavPath.OffsetPoint> list1 = list.subList(params.getA1(), params.getB1());
        // 第二阶段点位集
        List<UavPath.OffsetPoint> list2 = list.subList(params.getA2(), params.getB2());
        // 第三阶段点位集
        List<UavPath.OffsetPoint> list3 = list.subList(params.getA3(), params.getB3());

        Double h1 = list1.stream().collect(Collectors.averagingDouble(UavPath.OffsetPoint::getZ));
        Double h2 = list2.stream().collect(Collectors.averagingDouble(UavPath.OffsetPoint::getZ));
        Double h3 = list3.stream().collect(Collectors.averagingDouble(UavPath.OffsetPoint::getZ));

        AtomicReference<Double> s1 = new AtomicReference<>(0d);
        AtomicReference<Double> s2 = new AtomicReference<>(0d);
        AtomicReference<Double> s3 = new AtomicReference<>(0d);

        list1.forEach(item -> {
            s1.set(s1.get() + (Math.pow(item.getZ() - h1, 2)));
        });

        list2.forEach(item -> {
            s2.set(s2.get() + (Math.pow(item.getZ() - h2, 2)));
        });

        list3.forEach(item -> {
            s3.set(s3.get() + (Math.pow(item.getZ() - h3, 2)));
        });

        Double std1 = Math.sqrt((s1.get() / list1.size() - 1));
        Double std2 = Math.sqrt((s2.get() / list2.size() - 1));
        Double std3 = Math.sqrt((s3.get() / list3.size() - 1));
        Double min = std1 < std2 ? std1 : std2;
        min = min < std3 ? min : std3;

        return Double.isInfinite(min) || Double.isNaN(min) ? 0d : min;
    }

    /**
     * 速度保持性能
     *
     * @param params
     * @return
     */
    @Override
    public Double getSRP(HRPParams params) {
        List<UavPath.OffsetPoint> list = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        // 第一阶段点位集
        List<UavPath.OffsetPoint> list1 = list.subList(params.getA1(), params.getB1());
        // 第二阶段点位集
        List<UavPath.OffsetPoint> list2 = list.subList(params.getA2(), params.getB2());
        // 第三阶段点位集
        List<UavPath.OffsetPoint> list3 = list.subList(params.getA3(), params.getB3());

        Double s1 = 0d;
        for (int i = 0; i < params.getB1() - params.getA1() - 1; i++) {
            s1 = s1 + speed(list1.get(i), list1.get(i + 1));
        }
        s1 = s1 / list1.size();

        Double s2 = 0d;
        for (int i = 0; i < params.getB2() - params.getA2() - 1; i++) {
            s2 = s2 + speed(list2.get(i), list2.get(i + 1));
        }
        s2 = s2 / list2.size();

        Double s3 = 0d;
        for (int i = 0; i < params.getB3() - params.getA3() - 1; i++) {
            s3 = s3 + speed(list3.get(i), list3.get(i + 1));
        }
        s3 = s3 / list3.size();

        Double std1 = 0d;
        for (int i = 0; i < params.getB1() - params.getA1() - 1; i++) {
            std1 = std1 + (Math.pow(speed(list1.get(i), list1.get(i + 1)) - s1, 2));
        }
        Double std2 = 0d;
        for (int i = 0; i < params.getB2() - params.getA2() - 1; i++) {
            std2 = std2 + (Math.pow(speed(list2.get(i), list2.get(i + 1)) - s2, 2));
        }
        Double std3 = 0d;
        for (int i = 0; i < params.getB3() - params.getA3() - 1; i++) {
            std3 = std3 + (Math.pow(speed(list3.get(i), list3.get(i + 1)) - s3, 2));
        }

        std1 = Math.sqrt(std1 / (Math.abs(params.getB1() - params.getA1() - 1)));
        std2 = Math.sqrt(std2 / (Math.abs(params.getB2() - params.getA2() - 1)));
        std3 = Math.sqrt(std3 / (Math.abs(params.getB3() - params.getA3() - 1)));
        Double min = std1 < std2 ? std1 : std2;
        min = min < std3 ? min : std3;
        return Double.isInfinite(min) || Double.isNaN(min) ? 0d : min;
    }

    /**
     * 定位悬停
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Double> getFPH(FPHParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);
        List<UavPath.OffsetPoint> list4 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile4(), null, null, null);
        List<UavPath.OffsetPoint> list5 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile5(), null, null, null);

        //第一次测试 点集合
        List<UavPath.OffsetPoint> points1 = list1.subList(params.getA1(), params.getB1());
        //第二次测试 点集合
        List<UavPath.OffsetPoint> points2 = list2.subList(params.getA2(), params.getB2());
        //第三次测试 点集合
        List<UavPath.OffsetPoint> points3 = list3.subList(params.getA3(), params.getB3());
        //第四次测试 点集合
        List<UavPath.OffsetPoint> points4 = list4.subList(params.getA4(), params.getB4());
        //第五次测试 点集合
        List<UavPath.OffsetPoint> points5 = list5.subList(params.getA5(), params.getB5());
        // 第一次测试原点
        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        // 第二次测试原点
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        // 第三次测试原点
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        // 第四次测试原点
        UavPath.OffsetPoint a4 = list4.get(params.getA4());
        // 第五次测试原点
        UavPath.OffsetPoint a5 = list5.get(params.getA5());

        Double l1 = points1.stream().collect(Collectors.averagingDouble(item -> horizontalDistance(a1, item)));
        Double l2 = points2.stream().collect(Collectors.averagingDouble(item -> horizontalDistance(a2, item)));
        Double l3 = points3.stream().collect(Collectors.averagingDouble(item -> horizontalDistance(a3, item)));
        Double l4 = points4.stream().collect(Collectors.averagingDouble(item -> horizontalDistance(a4, item)));
        Double l5 = points5.stream().collect(Collectors.averagingDouble(item -> horizontalDistance(a5, item)));

        Double lStd1 = Math.sqrt(points1.stream().collect(Collectors.summingDouble(item -> Math.pow(horizontalDistance(a1, item) - l1, 2))) / points1.size() - 1);
        Double lStd2 = Math.sqrt(points2.stream().collect(Collectors.summingDouble(item -> Math.pow(horizontalDistance(a2, item) - l2, 2))) / points2.size() - 1);
        Double lStd3 = Math.sqrt(points3.stream().collect(Collectors.summingDouble(item -> Math.pow(horizontalDistance(a3, item) - l3, 2))) / points3.size() - 1);
        Double lStd4 = Math.sqrt(points4.stream().collect(Collectors.summingDouble(item -> Math.pow(horizontalDistance(a4, item) - l4, 2))) / points4.size() - 1);
        Double lStd5 = Math.sqrt(points5.stream().collect(Collectors.summingDouble(item -> Math.pow(horizontalDistance(a5, item) - l5, 2))) / points5.size() - 1);

        Double lAvg = (l1 + l2 + l3 + l4 + l5) / 5;
        Double lStd = (lStd1 + lStd2 + lStd3 + lStd4 + lStd5) / 5;

        Double h1 = points1.stream().collect(Collectors.averagingDouble(item -> Math.abs(item.getZ() - a1.getZ())));
        Double h2 = points2.stream().collect(Collectors.averagingDouble(item -> Math.abs(item.getZ() - a2.getZ())));
        Double h3 = points3.stream().collect(Collectors.averagingDouble(item -> Math.abs(item.getZ() - a3.getZ())));
        Double h4 = points4.stream().collect(Collectors.averagingDouble(item -> Math.abs(item.getZ() - a4.getZ())));
        Double h5 = points5.stream().collect(Collectors.averagingDouble(item -> Math.abs(item.getZ() - a5.getZ())));
        Double hAvg = (h1 + h2 + h3 + h4 + h5) / 5;

        Double hStd1 = Math.sqrt(points1.stream().collect(Collectors.summingDouble(item -> Math.pow(item.getZ() - h1, 2))) / points1.size() - 1);
        Double hStd2 = Math.sqrt(points2.stream().collect(Collectors.summingDouble(item -> Math.pow(item.getZ() - h2, 2))) / points2.size() - 1);
        Double hStd3 = Math.sqrt(points3.stream().collect(Collectors.summingDouble(item -> Math.pow(item.getZ() - h3, 2))) / points3.size() - 1);
        Double hStd4 = Math.sqrt(points4.stream().collect(Collectors.summingDouble(item -> Math.pow(item.getZ() - h4, 2))) / points4.size() - 1);
        Double hStd5 = Math.sqrt(points5.stream().collect(Collectors.summingDouble(item -> Math.pow(item.getZ() - h5, 2))) / points5.size() - 1);

        Double hStd = (hStd1 + hStd2 + hStd3 + hStd4 + hStd5) / 5;

        Map<String, Double> map = new HashMap<>();
        map.put("lAvg", Double.isInfinite(lAvg) || Double.isNaN(lAvg) ? 0 : lAvg);
        map.put("lStd", Double.isInfinite(lStd) || Double.isNaN(lStd) ? 0 : lStd);
        map.put("hAvg", Double.isInfinite(hAvg) || Double.isNaN(hAvg) ? 0 : hAvg);
        map.put("hStd", Double.isInfinite(hStd) || Double.isNaN(hStd) ? 0 : hStd);
        return map;
    }

    @Override
    public Long getNLH(NLHParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());

        long time1 = b1.getTime() - a1.getTime();
        long time2 = b2.getTime() - a2.getTime();
        long time3 = b3.getTime() - a3.getTime();
        long min = time1 < time2 ? time1 : time2;
        min = min < time3 ? min : time3;
        return min / 1000;
    }

    @Override
    public Long getNLP(NLHParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());

        long time1 = b1.getTime() - a1.getTime();
        long time2 = b2.getTime() - a2.getTime();
        long time3 = b3.getTime() - a3.getTime();
        long min = time1 < time2 ? time1 : time2;
        min = min < time3 ? min : time3;
        return min / 1000;
    }

    @Override
    public Long getFLH(NLHParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());

        long time1 = b1.getTime() - a1.getTime();
        long time2 = b2.getTime() - a2.getTime();
        long time3 = b3.getTime() - a3.getTime();
        long min = time1 < time2 ? time1 : time2;
        min = min < time3 ? min : time3;
        return min / 1000;
    }

    @Override
    public Long getFLP(NLHParams params) {
        List<UavPath.OffsetPoint> list1 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile1(), null, null, null);
        List<UavPath.OffsetPoint> list2 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile2(), null, null, null);
        List<UavPath.OffsetPoint> list3 = NMEAUtils.getOffsetPoints(uploadpath + File.separator + params.getFile3(), null, null, null);

        UavPath.OffsetPoint a1 = list1.get(params.getA1());
        UavPath.OffsetPoint b1 = list1.get(params.getB1());
        UavPath.OffsetPoint a2 = list2.get(params.getA2());
        UavPath.OffsetPoint b2 = list2.get(params.getB2());
        UavPath.OffsetPoint a3 = list3.get(params.getA3());
        UavPath.OffsetPoint b3 = list3.get(params.getB3());

        long time1 = b1.getTime() - a1.getTime();
        long time2 = b2.getTime() - a2.getTime();
        long time3 = b3.getTime() - a3.getTime();
        long min = time1 < time2 ? time1 : time2;
        min = min < time3 ? min : time3;
        return min / 1000;
    }


    /**
     * 计算两点距离（空间）
     *
     * @param a 点a
     * @param b 点b
     * @return
     */
    private double spaceDistance(UavPath.OffsetPoint a, UavPath.OffsetPoint b) {
        if (a == null || b == null) {
            return 0;
        }
        BigDecimal x1 = new BigDecimal(String.valueOf(a.getX()));
        BigDecimal x2 = new BigDecimal(String.valueOf(b.getX()));
        BigDecimal y1 = new BigDecimal(String.valueOf(a.getY()));
        BigDecimal y2 = new BigDecimal(String.valueOf(b.getY()));
        BigDecimal z1 = new BigDecimal(String.valueOf(a.getZ()));
        BigDecimal z2 = new BigDecimal(String.valueOf(b.getZ()));

        return Math.sqrt(Math.pow(x1.subtract(x2).doubleValue(), 2) + Math.pow(y1.subtract(y2).doubleValue(), 2) + Math.pow(z1.subtract(z2).doubleValue(), 2));
    }

    /**
     * 计算两点距离
     *
     * @param a
     * @param b
     * @return
     */
    private double horizontalDistance(UavPath.OffsetPoint a, UavPath.OffsetPoint b) {
        if (a == null || b == null) {
            return 0;
        }
        BigDecimal x1 = new BigDecimal(String.valueOf(a.getX()));
        BigDecimal x2 = new BigDecimal(String.valueOf(b.getX()));
        BigDecimal y1 = new BigDecimal(String.valueOf(a.getY()));
        BigDecimal y2 = new BigDecimal(String.valueOf(b.getY()));
        return Math.sqrt(Math.pow(x1.subtract(x2).doubleValue(), 2) + Math.pow(y1.subtract(y2).doubleValue(), 2));
    }

    /**
     * 两点获取速度
     *
     * @param a
     * @param b
     * @return
     */
    private double speed(UavPath.OffsetPoint a, UavPath.OffsetPoint b) {
        double m = spaceDistance(a, b);
        double s = (Math.abs(a.getTime() - b.getTime()) / 1000);
        return m / s;
    }
}
