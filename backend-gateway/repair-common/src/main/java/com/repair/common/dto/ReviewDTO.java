package com.repair.common.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReviewDTO {
    @NotNull(message = "报修单ID不能为空")
    private Long repairOrderId;
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    private Integer rating;
    private String comment;
}
