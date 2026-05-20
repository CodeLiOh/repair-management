package com.repair.review.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.common.dto.ReviewDTO;
import com.repair.common.entity.Review;
import com.repair.common.vo.StatisticsVO;

import java.util.List;
import java.util.Map;

public interface ReviewService extends IService<Review> {
    Review submit(ReviewDTO dto, Long userId);
    List<Map<String, Object>> getRepairerReviews(Long repairerId);
    Map<String, Object> getMyStats(Long repairerId);
    StatisticsVO getStatistics();
}
