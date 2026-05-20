package com.repair.repair;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.repair.repair", "com.repair.common"})
@MapperScan("com.repair.repair.mapper")
@EnableFeignClients(basePackages = "com.repair.common.feign")
public class RepairServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepairServiceApplication.class, args);
    }
}
