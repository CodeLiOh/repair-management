package com.repair.common.feign;

import com.repair.common.entity.User;
import com.repair.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserFeignClient {

    @GetMapping("/api/user/internal/{id}")
    Result<User> getById(@PathVariable("id") Long id);

    @GetMapping("/api/user/repairers")
    Result<List<User>> listRepairers();
}
