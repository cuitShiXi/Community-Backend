package com.shixi3.communitybackend.car.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shixi3.communitybackend.car.entity.Car;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CarMapper extends BaseMapper<Car> {

    Car getCarByOwner(Long owner);
    Car getAllCar();
    Car getVetCar();
}
