package com.repair.review.controller;

import com.repair.common.utils.Result;
import com.repair.common.dto.ReviewDTO;
import com.repair.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Result<?> submit(HttpServletRequest request, @Valid @RequestBody ReviewDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(reviewService.submit(dto, userId));
    }

    @GetMapping("/repairer/{id}")
    public Result<?> getRepairerReviews(@PathVariable Long id) {
        return Result.ok(reviewService.getRepairerReviews(id));
    }

    @GetMapping("/my-stats")
    public Result<?> getMyStats(HttpServletRequest request) {
        Long repairerId = (Long) request.getAttribute("userId");
        return Result.ok(reviewService.getMyStats(repairerId));
    }
}
