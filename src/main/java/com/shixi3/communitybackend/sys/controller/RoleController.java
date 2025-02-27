package com.shixi3.communitybackend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shixi3.communitybackend.common.exception.BizException;
import com.shixi3.communitybackend.common.model.CommonResult;
import com.shixi3.communitybackend.sys.entity.Role;
import com.shixi3.communitybackend.sys.entity.UserRole;
import com.shixi3.communitybackend.sys.mapper.RoleMapper;
import com.shixi3.communitybackend.sys.mapper.UserRoleMapper;
import com.shixi3.communitybackend.sys.service.RoleService;
import com.shixi3.communitybackend.sys.vo.UserRoleVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    RoleMapper roleMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    RoleService roleService;

    /**
     * 查询当前用户是什么角色
     *
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public CommonResult<Role> getRoleById(@PathVariable("id") Long id) {
        Role role = roleMapper.getRoleByUserID(id);
        return CommonResult.success(role);
    }


    /**
     * 查询所有角色
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public CommonResult<List<Role>> list() {
        return CommonResult.success(roleService.list());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public CommonResult<Page<Role>> pageSearch(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(required = false) String roleName) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(roleName != null, Role::getRoleName, roleName);
        Page<Role> result = roleService.page(new Page<>(page, pageSize), wrapper);
        return CommonResult.success(result);
    }

    /**
     * 编辑角色信息
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public CommonResult<String> updateRole(@RequestBody Role role, @PathVariable String id) {
        LambdaUpdateWrapper<Role> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(id != null, Role::getRoleId, id);
        boolean update = roleService.update(role, wrapper);
        if (!update) {
            throw new BizException("修改失败");
        }
        return CommonResult.success("修改成功");
    }

    /**
     * 添加角色信息
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('sys:role:add')")
    public CommonResult<String> addRole(@RequestBody Role role) {
        boolean save = roleService.save(role);
        if (!save) {
            throw new BizException("添加失败");
        }
        return CommonResult.success("添加成功");
    }

    /**
     * 删除角色信息
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public CommonResult<String> deleteRole(@PathVariable String id) {
        // 查询当前角色是否被使用
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getRoleId, id);
        List<UserRole> userRole = userRoleMapper.selectList(wrapper);
        if (!userRole.isEmpty()) {
            throw new BizException("无法删除，正在被使用");
        }
        boolean b = roleService.removeById(id);
        if (!b) {
            throw new BizException("删除失败");
        }
        return CommonResult.success("删除成功");
    }

    @Transactional
    @PutMapping("/updatePermissions/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:allot')")
    public CommonResult<String> allotPermissions(@PathVariable Long roleId, @RequestBody UserRoleVo userRoleVo) {
        boolean per = roleService.allotPermissions(userRoleVo.getPermissions(), roleId);
        if (!per) {
            throw new BizException("分配失败");
        }
        return CommonResult.success("分配成功");
    }
}
