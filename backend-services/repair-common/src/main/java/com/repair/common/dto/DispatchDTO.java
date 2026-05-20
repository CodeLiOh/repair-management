package com.repair.common.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class DispatchDTO {
    @NotNull(message = "报修单ID不能为空")
    private Long repairOrderId;
    @NotNull(message = "维修工ID不能为空")
    private Long repairerId;
}
