package com.shixi3.communitybackend.house.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("house")
public class House implements Serializable {

    /**
    * 房屋id
    */
    @TableId
    private Long houseId;
    /**
    * 户主id
    */
    private Long ownerId;
    /**
    * 楼栋
    */
    private Long buildingId;
    /**
    * 房号
    */
    private String houseNumber;
    /**
    * 房屋状态 0:空闲 1:已售出 2:出租中
    */

    private Integer state;

    private Date createTime;
    private Date updateTime;

}
