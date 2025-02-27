package com.shixi3.communitybackend.examine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shixi3.communitybackend.examine.entity.TenantExamineRecord;
import com.shixi3.communitybackend.examine.vo.HouseVetVo;

import java.util.List;

public interface HouseVetService extends IService<TenantExamineRecord> {
    Page<HouseVetVo> page(Integer page, Integer pageSize, Integer status);

    HouseVetVo getHouseVetVoById(Long id);

    List<HouseVetVo> getHouseVetVoByUserId(Long wxUserId);

    void auditHouseWithUser(HouseVetVo houseVetVo);
}
