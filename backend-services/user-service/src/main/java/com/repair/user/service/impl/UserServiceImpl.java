package com.repair.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.common.exception.BusinessException;
import com.repair.user.utils.JwtUtil;
import com.repair.common.dto.LoginDTO;
import com.repair.common.dto.RegisterDTO;
import com.repair.common.entity.User;
import com.repair.user.mapper.UserMapper;
import com.repair.user.service.UserService;
import com.repair.common.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }
        String md5Password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!md5Password.equals(user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginVO(token, user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public void register(RegisterDTO dto) {
        User exist = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setBuilding(dto.getBuilding());
        user.setUnitNum(dto.getUnitNum());
        user.setRoom(dto.getRoom());
        user.setRole("RESIDENT");
        user.setStatus(1);
        save(user);
    }

    @Override
    public User getCurrentUser(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateInfo(Long userId, User user) {
        User exist = getById(userId);
        if (exist == null) {
            throw new BusinessException("用户不存在");
        }
        user.setId(userId);
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
        user.setStatus(null);
        updateById(user);
    }

    @Override
    public List<User> listAll() {
        List<User> list = list();
        list.forEach(u -> u.setPassword(null));
        return list;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        updateById(user);
    }

    @Override
    public void updateRole(Long id, String role) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRole(role);
        updateById(user);
    }

    @Override
    public List<User> listRepairers() {
        List<User> list = list(new LambdaQueryWrapper<User>()
                .eq(User::getRole, "REPAIRER")
                .eq(User::getStatus, 1));
        list.forEach(u -> u.setPassword(null));
        return list;
    }

    @Override
    public User getByIdInternal(Long id) {
        User user = getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
}
