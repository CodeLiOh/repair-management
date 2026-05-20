package com.repair.common.feign;

import com.repair.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "review-service", url = "http://localhost:8084")
public interface ReviewFeignClient {

    @GetMapping("/api/review/internal/stats/{repairerId}")
    Result<Map<String, Object>> getRepairerStats(@PathVariable("repairerId") Long repairerId);
}
