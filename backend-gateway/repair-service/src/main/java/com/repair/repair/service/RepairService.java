package com.repair.repair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.common.dto.RepairOrderDTO;
import com.repair.common.entity.Category;
import com.repair.common.entity.RepairOrder;
import com.repair.common.vo.RepairOrderVO;

import java.util.List;

public interface RepairService extends IService<RepairOrder> {
    RepairOrder submit(RepairOrderDTO dto, Long userId);
    List<RepairOrderVO> listMyOrders(Long userId, String status);
    RepairOrderVO getDetail(Long id);
    void cancel(Long id, Long userId);
    List<RepairOrderVO> listPendingDispatch();
    List<Category> listCategories();
    Category addCategory(Category category);
    RepairOrder getOrderById(Long id);
    void updateOrderStatus(Long id, String status);
}
