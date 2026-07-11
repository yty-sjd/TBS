package com.example.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.game.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}