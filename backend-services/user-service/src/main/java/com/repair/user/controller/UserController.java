package com.repair.user.controller;

import com.repair.common.exception.BusinessException;
import com.repair.common.utils.Result;
import com.repair.common.dto.LoginDTO;
import com.repair.common.dto.RegisterDTO;
import com.repair.common.entity.User;
import com.repair.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.ok();
    }

    @GetMapping("/info")
    public Result<?> getInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(userService.getCurrentUser(userId));
    }

    @PutMapping("/info")
    public Result<?> updateInfo(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateInfo(userId, user);
        return Result.ok();
    }

    @GetMapping("/list")
    public Result<?> listAll(HttpServletRequest request) {
        checkAdmin(request);
        return Result.ok(userService.listAll());
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(HttpServletRequest request, @PathVariable Long id, @RequestParam Integer status) {
        checkAdmin(request);
        userService.updateStatus(id, status);
        return Result.ok();
    }

    @PutMapping("/{id}/role")
    public Result<?> updateRole(HttpServletRequest request, @PathVariable Long id, @RequestParam String role) {
        checkAdmin(request);
        userService.updateRole(id, role);
        return Result.ok();
    }

    @GetMapping("/repairers")
    public Result<?> listRepairers(HttpServletRequest request) {
        return Result.ok(userService.listRepairers());
    }

    @GetMapping("/internal/{id}")
    public Result<User> internalGetById(@PathVariable Long id) {
        return Result.ok(userService.getByIdInternal(id));
    }

    private void checkAdmin(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            throw new BusinessException(403, "无权限，仅管理员可操作");
        }
    }
}
