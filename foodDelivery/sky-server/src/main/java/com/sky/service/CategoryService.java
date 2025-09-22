package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/22 15:11
 */
public interface CategoryService {
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryDTO
     */

    void save(CategoryDTO categoryDTO);
    /**
     * 分类状态启用禁用
     * @param status
     * @param id
     */
    void statusSwitch(Integer status, Long id);
    /**
     * 删除分类
     * @param id
     */
    void delete(Long id);
    /**
     * 修改分类
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);
    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    Result<List<Category>> getByType(Integer type);
}
