package com.repair.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("repair_log")
public class RepairLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dispatchId;
    private String beforePhotos;
    private String afterPhotos;
    private String description;
    private String materialsUsed;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
