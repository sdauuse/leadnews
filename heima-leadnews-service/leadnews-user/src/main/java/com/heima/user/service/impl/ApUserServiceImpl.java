package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Autowired
    private ApUserMapper apUserMapper;

    @Override
    public ResponseResult login(LoginDto loginDto) {

        if (!ObjectUtils.isEmpty(loginDto.getPhone()) && !ObjectUtils.isEmpty(loginDto.getPassword())) {
            ApUser apUser = apUserMapper.selectOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, loginDto.getPhone()));
            if (apUser != null) {
                String salt = apUser.getSalt();
                String pwd = MD5Utils.encodeWithSalt(loginDto.getPassword(), salt);
                //如果密码相同，则上次jwt令牌
                if (pwd.equals(apUser.getPassword())) {
                    Map<String, Object> dataMap = new HashMap<>();
                    apUser.setPassword(null);
                    apUser.setSalt(null);
                    dataMap.put("user", apUser);
                    dataMap.put("token", AppJwtUtil.getToken(apUser.getId()));

                    return ResponseResult.okResult(dataMap);
                } else {
                    //密码错误
                    return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
                }
            } else {
                //用户信息不存在
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
        } else {
            //用户名和密码为空，为游客登录,id为0
            String token = AppJwtUtil.getToken(0L);
            return ResponseResult.okResult(token);
        }

    }
}
