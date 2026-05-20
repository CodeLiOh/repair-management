package com.repair.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.common.exception.BusinessException;
import com.repair.common.dto.DispatchDTO;
import com.repair.common.dto.RepairLogDTO;
import com.repair.common.entity.Dispatch;
import com.repair.common.entity.RepairLog;
import com.repair.common.entity.RepairOrder;
import com.repair.common.entity.User;
import com.repair.common.feign.RepairFeignClient;
import com.repair.common.feign.UserFeignClient;
import com.repair.common.utils.Result;
import com.repair.dispatch.mapper.DispatchMapper;
import com.repair.dispatch.mapper.RepairLogMapper;
import com.repair.dispatch.service.DispatchService;
import com.repair.common.vo.DispatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DispatchServiceImpl extends ServiceImpl<DispatchMapper, Dispatch> implements DispatchService {

    @Autowired
    private DispatchMapper dispatchMapper;
    @Autowired
    private RepairLogMapper repairLogMapper;
    @Autowired
    private RepairFeignClient repairFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    @Transactional
    public Dispatch dispatch(DispatchDTO dto, Long adminId) {
        Result<RepairOrder> orderResult = repairFeignClient.getOrderById(dto.getRepairOrderId());
        if (orderResult.getData() == null) {
            throw new BusinessException("报修单不存在");
        }
        RepairOrder order = orderResult.getData();
        if (!"PENDING_DISPATCH".equals(order.getStatus()) && !"PENDING_REVIEW".equals(order.getStatus())) {
            throw new BusinessException("当前报修单状态不可派单");
        }
        Result<User> repairerResult = userFeignClient.getById(dto.getRepairerId());
        if (repairerResult.getData() == null || !"REPAIRER".equals(repairerResult.getData().getRole())) {
            throw new BusinessException("维修工不存在");
        }
        Dispatch exist = getOne(new LambdaQueryWrapper<Dispatch>()
                .eq(Dispatch::getRepairOrderId, dto.getRepairOrderId()));
        if (exist != null) {
            throw new BusinessException("该报修单已派单");
        }
        Dispatch dispatch = new Dispatch();
        dispatch.setRepairOrderId(dto.getRepairOrderId());
        dispatch.setRepairerId(dto.getRepairerId());
        dispatch.setAdminId(adminId);
        dispatch.setStatus("PENDING_ACCEPT");
        dispatch.setDispatchTime(LocalDateTime.now());
        save(dispatch);
        repairFeignClient.updateOrderStatus(dto.getRepairOrderId(), "PENDING_ACCEPT");
        return dispatch;
    }

    @Override
    public List<DispatchVO> listMyTasks(Long repairerId, String status) {
        return dispatchMapper.selectVOList(repairerId, status);
    }

    @Override
    public DispatchVO getDispatchDetail(Long id) {
        DispatchVO vo = dispatchMapper.selectVOById(id);
        if (vo == null) {
            throw new BusinessException("派单记录不存在");
        }
        return vo;
    }

    @Override
    @Transactional
    public void accept(Long id, Long repairerId) {
        Dispatch dispatch = getById(id);
        if (dispatch == null) {
            throw new BusinessException("派单记录不存在");
        }
        if (!dispatch.getRepairerId().equals(repairerId)) {
            throw new BusinessException("只能接自己的任务");
        }
        if (!"PENDING_ACCEPT".equals(dispatch.getStatus())) {
            throw new BusinessException("当前状态不可接单");
        }
        dispatch.setStatus("ACCEPTED");
        dispatch.setAcceptTime(LocalDateTime.now());
        updateById(dispatch);
        repairFeignClient.updateOrderStatus(dispatch.getRepairOrderId(), "ACCEPTED");
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status, Long repairerId) {
        Dispatch dispatch = getById(id);
        if (dispatch == null) {
            throw new BusinessException("派单记录不存在");
        }
        if (!dispatch.getRepairerId().equals(repairerId)) {
            throw new BusinessException("只能更新自己的任务");
        }
        dispatch.setStatus(status);
        if ("COMPLETED".equals(status)) {
            dispatch.setCompleteTime(LocalDateTime.now());
        }
        updateById(dispatch);
        String orderStatus = null;
        if ("REPAIRING".equals(status)) {
            orderStatus = "REPAIRING";
        } else if ("COMPLETED".equals(status)) {
            orderStatus = "PENDING_CHECK";
        }
        if (orderStatus != null) {
            repairFeignClient.updateOrderStatus(dispatch.getRepairOrderId(), orderStatus);
        }
    }

    @Override
    public RepairLog addLog(Long dispatchId, RepairLogDTO dto) {
        Dispatch dispatch = getById(dispatchId);
        if (dispatch == null) {
            throw new BusinessException("派单记录不存在");
        }
        RepairLog log = new RepairLog();
        log.setDispatchId(dispatchId);
        log.setBeforePhotos(dto.getBeforePhotos());
        log.setAfterPhotos(dto.getAfterPhotos());
        log.setDescription(dto.getDescription());
        log.setMaterialsUsed(dto.getMaterialsUsed());
        repairLogMapper.insert(log);
        return log;
    }

    @Override
    public List<RepairLog> getLogs(Long dispatchId) {
        return repairLogMapper.selectList(new LambdaQueryWrapper<RepairLog>()
                .eq(RepairLog::getDispatchId, dispatchId)
                .orderByDesc(RepairLog::getCreateTime));
    }

    @Override
    @Transactional
    public void transfer(Long id, Long repairerId, String reason) {
        Dispatch dispatch = getById(id);
        if (dispatch == null) {
            throw new BusinessException("派单记录不存在");
        }
        if (!dispatch.getRepairerId().equals(repairerId)) {
            throw new BusinessException("只能转自己的任务");
        }
        dispatch.setStatus("TRANSFERRED");
        dispatch.setTransferReason(reason);
        updateById(dispatch);
        repairFeignClient.updateOrderStatus(dispatch.getRepairOrderId(), "PENDING_DISPATCH");
    }

    @Override
    @Transactional
    public void reDispatch(Long id, Long newRepairerId, Long adminId) {
        Dispatch old = getById(id);
        if (old == null) {
            throw new BusinessException("派单记录不存在");
        }
        Result<User> repairerResult = userFeignClient.getById(newRepairerId);
        if (repairerResult.getData() == null || !"REPAIRER".equals(repairerResult.getData().getRole())) {
            throw new BusinessException("维修工不存在");
        }
        old.setRepairerId(newRepairerId);
        old.setAdminId(adminId);
        old.setStatus("PENDING_ACCEPT");
        old.setDispatchTime(LocalDateTime.now());
        old.setAcceptTime(null);
        old.setCompleteTime(null);
        old.setTransferReason(null);
        updateById(old);
        repairFeignClient.updateOrderStatus(old.getRepairOrderId(), "PENDING_ACCEPT");
    }

    @Override
    public Dispatch getDispatchByOrderId(Long orderId) {
        return getOne(new LambdaQueryWrapper<Dispatch>()
                .eq(Dispatch::getRepairOrderId, orderId));
    }
}
