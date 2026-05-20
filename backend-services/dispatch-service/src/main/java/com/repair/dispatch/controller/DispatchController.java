package com.repair.dispatch.controller;

import com.repair.common.utils.Result;
import com.repair.common.dto.DispatchDTO;
import com.repair.common.dto.RepairLogDTO;
import com.repair.dispatch.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    @PostMapping
    public Result<?> dispatch(HttpServletRequest request, @Valid @RequestBody DispatchDTO dto) {
        checkRole(request, "ADMIN");
        Long adminId = (Long) request.getAttribute("userId");
        return Result.ok(dispatchService.dispatch(dto, adminId));
    }

    @GetMapping("/my-tasks")
    public Result<?> listMyTasks(HttpServletRequest request,
                                  @RequestParam(required = false) String status) {
        checkRole(request, "REPAIRER");
        Long repairerId = (Long) request.getAttribute("userId");
        return Result.ok(dispatchService.listMyTasks(repairerId, status));
    }

    @GetMapping("/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return Result.ok(dispatchService.getDispatchDetail(id));
    }

    @PutMapping("/{id}/accept")
    public Result<?> accept(HttpServletRequest request, @PathVariable Long id) {
        Long repairerId = (Long) request.getAttribute("userId");
        dispatchService.accept(id, repairerId);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(HttpServletRequest request, @PathVariable Long id, @RequestParam String status) {
        checkRole(request, "REPAIRER");
        Long repairerId = (Long) request.getAttribute("userId");
        dispatchService.updateStatus(id, status, repairerId);
        return Result.ok();
    }

    @PostMapping("/{id}/log")
    public Result<?> addLog(HttpServletRequest request, @PathVariable Long id, @RequestBody RepairLogDTO dto) {
        return Result.ok(dispatchService.addLog(id, dto));
    }

    @GetMapping("/{id}/logs")
    public Result<?> getLogs(@PathVariable Long id) {
        return Result.ok(dispatchService.getLogs(id));
    }

    @PutMapping("/{id}/transfer")
    public Result<?> transfer(HttpServletRequest request, @PathVariable Long id, @RequestParam String reason) {
        checkRole(request, "REPAIRER");
        Long repairerId = (Long) request.getAttribute("userId");
        dispatchService.transfer(id, repairerId, reason);
        return Result.ok();
    }

    @PutMapping("/{id}/re-dispatch")
    public Result<?> reDispatch(HttpServletRequest request, @PathVariable Long id, @RequestParam Long newRepairerId) {
        checkRole(request, "ADMIN");
        Long adminId = (Long) request.getAttribute("userId");
        dispatchService.reDispatch(id, newRepairerId, adminId);
        return Result.ok();
    }

    @GetMapping("/internal/by-order/{orderId}")
    public Result<?> internalGetDispatchByOrderId(@PathVariable Long orderId) {
        return Result.ok(dispatchService.getDispatchByOrderId(orderId));
    }

    private void checkRole(HttpServletRequest request, String role) {
        String r = (String) request.getAttribute("role");
        if (!role.equals(r)) {
            throw new RuntimeException("无权限");
        }
    }
}
