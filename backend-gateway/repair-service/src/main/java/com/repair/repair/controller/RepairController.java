package com.repair.repair.controller;

import com.repair.common.utils.Result;
import com.repair.common.dto.RepairOrderDTO;
import com.repair.common.entity.Category;
import com.repair.repair.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @PostMapping
    public Result<?> submit(HttpServletRequest request, @Valid @RequestBody RepairOrderDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(repairService.submit(dto, userId));
    }

    @GetMapping("/my")
    public Result<?> listMyOrders(HttpServletRequest request,
                                  @RequestParam(required = false) String status) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(repairService.listMyOrders(userId, status));
    }

    @GetMapping("/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return Result.ok(repairService.getDetail(id));
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        repairService.cancel(id, userId);
        return Result.ok();
    }

    @GetMapping("/pending")
    public Result<?> listPendingDispatch(HttpServletRequest request) {
        checkRole(request, "ADMIN");
        return Result.ok(repairService.listPendingDispatch());
    }

    @GetMapping("/categories")
    public Result<?> listCategories() {
        return Result.ok(repairService.listCategories());
    }

    @PostMapping("/categories")
    public Result<?> addCategory(HttpServletRequest request, @RequestBody Category category) {
        checkRole(request, "ADMIN");
        return Result.ok(repairService.addCategory(category));
    }

    @GetMapping("/internal/{id}")
    public Result<?> internalGetOrderById(@PathVariable Long id) {
        return Result.ok(repairService.getOrderById(id));
    }

    @PutMapping("/internal/{id}/status")
    public Result<Void> internalUpdateStatus(@PathVariable Long id, @RequestParam String status) {
        repairService.updateOrderStatus(id, status);
        return Result.ok();
    }

    private void checkRole(HttpServletRequest request, String role) {
        String r = (String) request.getAttribute("role");
        if (!role.equals(r)) {
            throw new RuntimeException("无权限");
        }
    }
}
