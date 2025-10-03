package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;
import org.springframework.stereotype.Service;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/3 18:09
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userLoginDTO 登录参数
     * @return 登录结果
     */
    Result<UserLoginVO> login(UserLoginDTO userLoginDTO);
}
