package com.repair.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("repair_order")
public class RepairOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long categoryId;
    private String description;
    private String urgency;
    private String status;
    private String images;
    private LocalDateTime appointTime;
    private String cancelReason;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
