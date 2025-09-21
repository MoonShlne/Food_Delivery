package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {


    /**
     * 分页查询实现
     * @param employeePage
     * @param name
     * @return
     */
    Page<Employee> pageQuery(Page<Employee> employeePage,@Param("name") String name);






//已经用mybatis plus 直接在service 层解决
//    /**
//     * 根据用户名查询员工
//     * @param username
//     * @return
//     */
//    @Select("select * from employee where username = #{username}")
//    Employee getByUsername(String username);


}
