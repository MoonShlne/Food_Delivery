package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/29 18:59
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {

        //先把套餐基础信息加入数据库
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        this.save(setmeal);

        //再把套餐的里面的菜品信息加入数据库
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(
                Dishes -> {
                    Dishes.setSetmealId(setmeal.getId());
                    setMealDishMapper.insert(Dishes);
                }
        );

    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        Page<Setmeal> page = new Page<>(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        IPage<SetmealVO> setmealPage = setMealMapper.pageQuery(page, setmealPageQueryDTO);

        setmealPage.getTotal();
        setmealPage.getRecords();

        return Result.success(new PageResult(setmealPage.getTotal(), setmealPage.getRecords()));


    }

    /**
     * 套餐状态修改
     *
     * @param status
     * @param id
     */
    @Override
    public void statusSwitch(Integer status, Long id) {
        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Setmeal::getId, id).set(Setmeal::getStatus, status);
        setMealMapper.update(wrapper);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        //先提供套餐基础信息
        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        //再提供套餐里面的菜品信息
        LambdaUpdateWrapper<SetmealDish> setmealDishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealDishLambdaUpdateWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setMealDishMapper.selectList(setmealDishLambdaUpdateWrapper);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        //修改套餐的基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        this.updateById(setmeal);

        //删除套餐对应的菜品信息
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        setMealDishMapper.delete(wrapper);

        //添加套餐对应的菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(
                Dishes -> {
                    Dishes.setSetmealId(setmeal.getId());
                    setMealDishMapper.insert(Dishes);
                }
        );

    }

    @Override
    @Transactional
    public void deleteWithDishes(List<Long> ids) {
        //删除套餐
        removeByIds(ids);

        //删除套餐对应的菜品信息
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setMealDishMapper.delete(dishLambdaQueryWrapper);


    }


}
