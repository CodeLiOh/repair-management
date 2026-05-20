package com.repair.common.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RepairOrderVO {
    private Long id;
    private Long userId;
    private String userName;
    private String userPhone;
    private String building;
    private String unitNum;
    private String room;
    private Long categoryId;
    private String categoryName;
    private String description;
    private String urgency;
    private String status;
    private String images;
    private LocalDateTime appointTime;
    private String cancelReason;
    private String repairerName;
    private Integer rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
