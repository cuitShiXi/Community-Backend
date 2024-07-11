package com.shixi3.communitybackend.examine.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixi3.communitybackend.Family.entity.UserHouse;
import com.shixi3.communitybackend.Family.entity.WxUser;
import com.shixi3.communitybackend.Family.service.UserHouseService;
import com.shixi3.communitybackend.Family.service.WxUserService;
import com.shixi3.communitybackend.examine.entity.TenantExamineRecord;
import com.shixi3.communitybackend.examine.mapper.HouseVetMapper;
import com.shixi3.communitybackend.examine.service.HouseVetService;
import com.shixi3.communitybackend.examine.vo.HouseVetVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseVetServiceImpl extends ServiceImpl<HouseVetMapper, TenantExamineRecord> implements HouseVetService {
    @Resource
    private HouseVetMapper houseVetMapper;
    @Resource
    private WxUserService wxUserService;
    @Resource
    private UserHouseService userHouseService;
    @Override
    public Page<HouseVetVo> page(Integer page, Integer pageSize, Integer status) {


        Page<HouseVetVo> result = new Page<>(page,pageSize);
        result = houseVetMapper.page(result,status);
        return result;
    }

    @Override
    public HouseVetVo getHouseVetVoById(Long id) {
        return houseVetMapper.getHouseVetVoById(id);
    }
    @Override
    public List<HouseVetVo> getHouseVetVoByUserId(Long wxUserId) {
        return houseVetMapper.getHouseVetVoByUserId(wxUserId);
    }
    @Override
    public void auditHouseWithUser(HouseVetVo houseVetVo) {
        Integer status = houseVetVo.getStatus();
        // 修改状态
        this.updateById(houseVetVo);
        // 审核通过
        if(status == 2) {
            Long userId = houseVetVo.getWxUserId();
            // 获取用户身份
            LambdaQueryWrapper<WxUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WxUser::getId,userId);
            WxUser wxUser = wxUserService.getOne(wrapper);
            // 修改用户类型
            if(wxUser.getUserType() != 0) {
                wxUser.setUserType(houseVetVo.getUserType());
                wxUserService.updateById(wxUser);
            }
            // 添加房屋用户关系
            Integer type = houseVetVo.getUserType();
            Integer belongFlag = 0;
            if(type == 1) belongFlag = 2;
            UserHouse userHouse = new UserHouse();
            userHouse.setWxUserId(wxUser.getId());
            userHouse.setHouseId(houseVetVo.getHouseId());
            userHouse.setBelongFlag(belongFlag);
            userHouseService.save(userHouse);
        }
    }
}
