package com.repair.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.common.exception.BusinessException;
import com.repair.common.dto.ReviewDTO;
import com.repair.common.entity.Dispatch;
import com.repair.common.entity.RepairOrder;
import com.repair.common.entity.Review;
import com.repair.common.entity.User;
import com.repair.common.feign.DispatchFeignClient;
import com.repair.common.feign.RepairFeignClient;
import com.repair.common.feign.UserFeignClient;
import com.repair.common.utils.Result;
import com.repair.review.mapper.ReviewMapper;
import com.repair.review.service.ReviewService;
import com.repair.common.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private RepairFeignClient repairFeignClient;
    @Autowired
    private DispatchFeignClient dispatchFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    @Transactional
    public Review submit(ReviewDTO dto, Long userId) {
        Result<RepairOrder> orderResult = repairFeignClient.getOrderById(dto.getRepairOrderId());
        if (orderResult.getData() == null) {
            throw new BusinessException("报修单不存在");
        }
        RepairOrder order = orderResult.getData();
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("只能评价自己的报修单");
        }
        if (!"PENDING_CHECK".equals(order.getStatus()) && !"COMPLETED".equals(order.getStatus())) {
            throw new BusinessException("当前状态不可评价");
        }
        Review exist = getOne(new LambdaQueryWrapper<Review>()
                .eq(Review::getRepairOrderId, dto.getRepairOrderId()));
        if (exist != null) {
            throw new BusinessException("已评价过该报修单");
        }
        Result<Dispatch> dispatchResult = dispatchFeignClient.getDispatchByOrderId(dto.getRepairOrderId());
        if (dispatchResult.getData() == null) {
            throw new BusinessException("未找到派单记录");
        }
        Dispatch dispatch = dispatchResult.getData();
        Review review = new Review();
        review.setRepairOrderId(dto.getRepairOrderId());
        review.setUserId(userId);
        review.setRepairerId(dispatch.getRepairerId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        save(review);
        repairFeignClient.updateOrderStatus(dto.getRepairOrderId(), "COMPLETED");
        return review;
    }

    @Override
    public List<Map<String, Object>> getRepairerReviews(Long repairerId) {
        return reviewMapper.selectByRepairerId(repairerId);
    }

    @Override
    public Map<String, Object> getMyStats(Long repairerId) {
        Map<String, Object> stats = reviewMapper.selectStatsByRepairerId(repairerId);
        if (stats == null) {
            stats = new HashMap<>();
            stats.put("total", 0);
            stats.put("avg_rating", 0);
        }
        // 通过 Feign 获取已完成派单数需要 dispatch-service 暴露统计接口
        stats.put("completed_orders", 0);
        return stats;
    }

    @Override
    public StatisticsVO getStatistics() {
        StatisticsVO vo = new StatisticsVO();
        // Query from repair-service via Feign for order counts
        // For simplicity, use local review table counts + Feign queries
        List<User> repairers = null;
        Result<List<User>> repairersResult = userFeignClient.listRepairers();
        if (repairersResult.getData() != null) {
            repairers = repairersResult.getData();
        } else {
            repairers = new ArrayList<>();
        }

        vo.setTotalRepairers((long) repairers.size());
        vo.setCategoryDistribution(new ArrayList<>());

        List<Map<String, Object>> ranking = new ArrayList<>();
        for (User r : repairers) {
            Map<String, Object> m = new HashMap<>();
            m.put("repairerId", r.getId());
            m.put("repairerName", r.getRealName());
            Double avg = reviewMapper.getAvgRatingByRepairerId(r.getId());
            m.put("avgRating", avg != null ? avg : 0);
            long count = reviewMapper.selectCount(
                    new LambdaQueryWrapper<Review>().eq(Review::getRepairerId, r.getId()));
            m.put("reviewCount", count);
            ranking.add(m);
        }
        ranking.sort((a, b) -> Double.compare((Double) b.get("avgRating"), (Double) a.get("avgRating")));
        vo.setRepairerRanking(ranking);
        vo.setMonthlyTrend(new ArrayList<>());
        return vo;
    }
}
