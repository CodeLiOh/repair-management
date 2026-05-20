package com.repair.common.dto;

import lombok.Data;

@Data
public class RepairLogDTO {
    private String beforePhotos;
    private String afterPhotos;
    private String description;
    private String materialsUsed;
}
