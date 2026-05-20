package com.repair.repair.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.common.exception.BusinessException;
import com.repair.common.dto.RepairOrderDTO;
import com.repair.common.entity.Category;
import com.repair.common.entity.RepairOrder;
import com.repair.repair.mapper.CategoryMapper;
import com.repair.repair.mapper.RepairOrderMapper;
import com.repair.repair.service.RepairService;
import com.repair.common.vo.RepairOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public RepairOrder submit(RepairOrderDTO dto, Long userId) {
        RepairOrder order = new RepairOrder();
        order.setUserId(userId);
        order.setCategoryId(dto.getCategoryId());
        order.setDescription(dto.getDescription());
        order.setUrgency(dto.getUrgency());
        order.setImages(dto.getImages());
        order.setStatus("PENDING_REVIEW");
        if (dto.getAppointTime() != null && !dto.getAppointTime().isEmpty()) {
            order.setAppointTime(java.time.LocalDateTime.parse(dto.getAppointTime()));
        }
        save(order);
        return order;
    }

    @Override
    public List<RepairOrderVO> listMyOrders(Long userId, String status) {
        return repairOrderMapper.selectVOList(status, userId);
    }

    @Override
    public RepairOrderVO getDetail(Long id) {
        RepairOrderVO vo = repairOrderMapper.selectVOById(id);
        if (vo == null) {
            throw new BusinessException("报修单不存在");
        }
        return vo;
    }

    @Override
    public void cancel(Long id, Long userId) {
        RepairOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("报修单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("只能撤销自己的报修单");
        }
        if (!"PENDING_REVIEW".equals(order.getStatus()) && !"PENDING_DISPATCH".equals(order.getStatus())) {
            throw new BusinessException("当前状态不可撤销");
        }
        order.setStatus("CANCELLED");
        updateById(order);
    }

    @Override
    public List<RepairOrderVO> listPendingDispatch() {
        return repairOrderMapper.selectVOList("PENDING_DISPATCH", null);
    }

    @Override
    public List<Category> listCategories() {
        return categoryMapper.selectList(null);
    }

    @Override
    public Category addCategory(Category category) {
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public RepairOrder getOrderById(Long id) {
        RepairOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("报修单不存在");
        }
        return order;
    }

    @Override
    public void updateOrderStatus(Long id, String status) {
        RepairOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("报修单不存在");
        }
        order.setStatus(status);
        updateById(order);
    }
}
