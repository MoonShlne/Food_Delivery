package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/22 15:07
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{},{}", categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        return categoryService.pageQuery(categoryPageQueryDTO);
    }

    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @PostMapping("status/{status}")
    @ApiOperation(value = "分类状态启用禁用")
    public Result statusSwitch(@PathVariable("status") Integer status, Long id) {
        log.info("分类状态启用禁用:{},{}", status, id);
        categoryService.statusSwitch(status, id);

        return Result.success();

    }

@DeleteMapping
@ApiOperation(value = "删除分类")
    public Result delete(Long id) {
        log.info("删除分类:{}", id);
        categoryService.delete(id);
        return Result.success();
    }



    @PutMapping
    @ApiOperation(value = "修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation(value = "根据条件查询分类")
    public Result<List<Category>> getByType(Integer type){
     log.info("根据条件查询分类:{}",type);

     Result<List<Category>> result= categoryService.getByType(type);
     return result;


    }
}
