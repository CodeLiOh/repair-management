package com.repair.common.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsVO {
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private Long totalRepairers;
    private List<Map<String, Object>> categoryDistribution;
    private List<Map<String, Object>> repairerRanking;
    private List<Map<String, Object>> monthlyTrend;
}
