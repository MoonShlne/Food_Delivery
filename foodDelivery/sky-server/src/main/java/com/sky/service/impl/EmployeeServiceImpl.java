package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        //  employee = employeeMapper.getByUsername(username);

        //使用mybatis plus 的lambda query wrapper来查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(wrapper);


        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }


        //密码比对
        //
        String encPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encPassword.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //把传来的属性复制给employee
        BeanUtils.copyProperties(employeeDTO, employee);
        //给其他空属性赋值
        //设置账号的状态
        employee.setStatus(StatusConstant.ENABLE);
        //创建密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //创建时间
        employee.setCreateTime(LocalDateTime.now());
        //更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //createUser
        employee.setCreateUser(BaseContext.getCurrentId());
        //updateUser
        employee.setUpdateUser(BaseContext.getCurrentId());
        //把传来的新角色注册到数据库
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        Page<Employee> employeePage = new Page<>(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePage, employeePageQueryDTO.getName());

        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 员工状态修改
     *
     * @param status
     * @param id
     */
    @Override
    public void statusSwitch(Integer status, Long id) {
//        //使用wrapper
//        LambdaUpdateWrapper<Employee> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.set(Employee::getStatus, status).eq(Employee::getId, id);
//        employeeMapper.update(null, wrapper);

//        方法2  使用自带的update方法
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        employeeMapper.updateById(employee);


    }

}
