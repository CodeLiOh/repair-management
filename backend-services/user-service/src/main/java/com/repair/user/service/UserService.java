package com.repair.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.common.dto.LoginDTO;
import com.repair.common.dto.RegisterDTO;
import com.repair.common.entity.User;
import com.repair.common.vo.LoginVO;

import java.util.List;

public interface UserService extends IService<User> {
    LoginVO login(LoginDTO dto);
    void register(RegisterDTO dto);
    User getCurrentUser(Long userId);
    void updateInfo(Long userId, User user);
    List<User> listAll();
    void updateStatus(Long id, Integer status);
    void updateRole(Long id, String role);
    List<User> listRepairers();
    User getByIdInternal(Long id);
}
