package com.heima.user.controller;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.user.service.ApUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ApUserController", description = "用户api接口")
@RestController()
@RequestMapping(value = "/api/v1")
public class ApUserController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/login/login_auth")
    public ResponseResult login(@RequestBody LoginDto loginDto) {

        return apUserService.login(loginDto);
    }
}
