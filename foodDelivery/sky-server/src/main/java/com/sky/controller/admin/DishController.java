package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/27 14:32
 * 菜品接口
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        //清理redis缓存
        String key="dish_"+dishDTO.getCategoryId();
        deleteCatch(key);

        dishService.save(dishDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        log.info("分页查询菜品:{}", dishPageQueryDTO);

        return dishService.pageQuery(dishPageQueryDTO);

    }

    @DeleteMapping
    @ApiOperation(value = "删除/批量删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        //清理redis缓存
        deleteCatch("dish_*");

        log.info("删除/批量删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}", id);
        return dishService.getByIdWithFlavor(id);
    }

    @PutMapping
    @ApiOperation(value = "修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        //清理redis缓存
        deleteCatch("dish_*");

        dishService.update(dishDTO);
        return Result.success();
    }


    @PostMapping("/status/{status}")
    @ApiOperation(value = "起售/停售菜品")
    public Result statusSwitch(@PathVariable Integer status, Long id) {
        log.info("起售/停售菜品:{}", id, status);
        //清理redis缓存
        deleteCatch("dish_*");

        dishService.statusSwitch(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(Long categoryId) {
        log.info("根据分类id查询菜品:{}", categoryId);
        List<Dish> dishVOS = dishService.list(categoryId);
        return Result.success(dishVOS);
    }



    /**
     * 清理redis缓存
     * 在菜品 增加  删除  修改  起售 停售 时
     * @param pattern
     */
    private void deleteCatch(String pattern){
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
