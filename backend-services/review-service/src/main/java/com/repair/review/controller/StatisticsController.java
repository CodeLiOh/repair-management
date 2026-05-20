package com.repair.review.controller;

import com.repair.common.utils.Result;
import com.repair.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public Result<?> getStatistics(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("无权限，仅管理员可操作");
        }
        return Result.ok(reviewService.getStatistics());
    }
}
