package com.repair.common.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RepairOrderDTO {
    @NotNull(message = "报修分类不能为空")
    private Long categoryId;
    @NotBlank(message = "故障描述不能为空")
    private String description;
    @NotBlank(message = "紧急程度不能为空")
    private String urgency;
    private String images;
    private String appointTime;
}
