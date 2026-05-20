package com.repair.common.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DispatchVO {
    private Long id;
    private Long repairOrderId;
    private Long repairerId;
    private String repairerName;
    private String repairerPhone;
    private String skillTag;
    private Long adminId;
    private String adminName;
    private String status;
    private String repairOrderDesc;
    private String urgency;
    private Long userId;
    private String userName;
    private String userPhone;
    private String building;
    private String unitNum;
    private String room;
    private String categoryName;
    private LocalDateTime dispatchTime;
    private LocalDateTime acceptTime;
    private LocalDateTime completeTime;
    private String transferReason;
}
