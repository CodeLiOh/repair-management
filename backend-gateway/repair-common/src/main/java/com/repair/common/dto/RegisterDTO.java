package com.repair.common.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "姓名不能为空")
    private String realName;
    @NotBlank(message = "手机号不能为空")
    private String phone;
    private String email;
    private String building;
    private String unitNum;
    private String room;
}
