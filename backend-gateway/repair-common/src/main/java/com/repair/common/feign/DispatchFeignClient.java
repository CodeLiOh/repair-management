package com.repair.common.feign;

import com.repair.common.entity.Dispatch;
import com.repair.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dispatch-service", url = "http://localhost:8083")
public interface DispatchFeignClient {

    @GetMapping("/api/dispatch/internal/by-order/{orderId}")
    Result<Dispatch> getDispatchByOrderId(@PathVariable("orderId") Long orderId);
}
