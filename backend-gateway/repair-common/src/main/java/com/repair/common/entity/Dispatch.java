package com.repair.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dispatch")
public class Dispatch {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long repairOrderId;
    private Long repairerId;
    private Long adminId;
    private String status;
    private LocalDateTime dispatchTime;
    private LocalDateTime acceptTime;
    private LocalDateTime completeTime;
    private String transferReason;
}
