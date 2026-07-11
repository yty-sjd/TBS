package com.example.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.entity.Role;
import com.example.game.repository.RoleRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 查询所有角色
     * GET /api/admin/roles
     */
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * 根据ID查询角色
     * GET /api/admin/roles/{id}
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Integer id) {
        return roleRepository.findById(id)
                .map(role -> ResponseEntity.ok(role))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 批量查询角色（根据ID列表，从数据库获取原始数据）
     * POST /api/admin/roles/batch
     */
    @PostMapping("/roles/batch")
    public List<Role> getRolesByIds(@RequestBody List<Integer> ids) {
        return roleRepository.findAllById(ids);
    }

    /**
     * 创建新角色
     * POST /api/admin/roles
     */
    @PostMapping("/roles")
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    /**
     * 更新角色
     * PUT /api/admin/roles/{id}
     */
    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Integer id, @RequestBody Role updatedRole) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setRoleName(updatedRole.getRoleName());
                    existingRole.setHp(updatedRole.getHp());
                    existingRole.setHpMax(updatedRole.getHpMax());
                    existingRole.setAttack(updatedRole.getAttack());
                    existingRole.setSkillId(updatedRole.getSkillId());
                    existingRole.setPassiveId(updatedRole.getPassiveId());
                    existingRole.setSkillDescription(updatedRole.getSkillDescription());
                    existingRole.setPassiveDescription(updatedRole.getPassiveDescription());
                    existingRole.setRemark(updatedRole.getRemark());
                    existingRole.setPortraitId(updatedRole.getPortraitId());
                    return ResponseEntity.ok(roleRepository.save(existingRole));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 删除角色
     * DELETE /api/admin/roles/{id}
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Integer id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}