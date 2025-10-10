package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/3 18:13
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private WeChatProperties weChatProperties;


    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param userLoginDTO 登录参数
     * @return 登录结果
     */
    @Override
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        log.info("用户登录，参数为{}", userLoginDTO);
        //调用微信登录接口，获取用户信息
        User user = wxLogin(userLoginDTO.getCode());

        //为用户生成token
        HashMap claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        //包装返回值
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(user.getId());
        userLoginVO.setOpenid(user.getOpenid());
        userLoginVO.setToken(token);


        return Result.success(userLoginVO);
    }

    /**
     * 微信登录
     * 获取微信用户信息
     *
     * @param code 登录请求码
     * @return 用户信息
     */
    private User wxLogin(String code) {
        // 1. 调用微信登录接口，获取openid
        String openid = getOpenId(code);
        log.info("调用微信登录接口，获取openid，结果为{}", openid);
        //判断是否获取到openid
        if (openid == null) {
            throw new RuntimeException(MessageConstant.LOGIN_FAILED);
        }
        // 2. 根据openid查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);
        // 3. 如果用户不存在，创建新用户 并且返回
        if (Objects.isNull(user)) {
            User newUser = new User();
            newUser.setOpenid(openid);
            userMapper.insert(newUser);
            //mybatis-plus会自动回填id
            return newUser;
        }
        // 4. 如果用户存在，直接返回用户信息
        return user;

    }

    /**
     * 微信登录
     * 调用微信登录接口，获取用户openid
     *
     * @param code 登录请求码
     * @return 用户信息
     */
    private String getOpenId(String code) {
        // 1. 调用微信登录接口，获取openid
        log.info("调用微信登录接口，获取openid，code为{}", code);
        HashMap<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, params);

        JSONObject jsonObject = JSON.parseObject(json);

        return jsonObject.getString("openid");
    }
}
