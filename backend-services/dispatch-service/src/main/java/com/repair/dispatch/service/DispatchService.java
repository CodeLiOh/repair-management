package com.repair.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.common.dto.DispatchDTO;
import com.repair.common.dto.RepairLogDTO;
import com.repair.common.entity.Dispatch;
import com.repair.common.entity.RepairLog;
import com.repair.common.vo.DispatchVO;

import java.util.List;

public interface DispatchService extends IService<Dispatch> {
    Dispatch dispatch(DispatchDTO dto, Long adminId);
    List<DispatchVO> listMyTasks(Long repairerId, String status);
    DispatchVO getDispatchDetail(Long id);
    void accept(Long id, Long repairerId);
    void updateStatus(Long id, String status, Long repairerId);
    RepairLog addLog(Long dispatchId, RepairLogDTO dto);
    List<RepairLog> getLogs(Long dispatchId);
    void transfer(Long id, Long repairerId, String reason);
    void reDispatch(Long id, Long newRepairerId, Long adminId);
    Dispatch getDispatchByOrderId(Long orderId);
}
