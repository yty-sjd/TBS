package com.example.game.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.game.service.tbs_entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}