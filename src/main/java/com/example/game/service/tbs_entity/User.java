package com.example.game.service.tbs_entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 用户实体类
 * 映射数据库表 users
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "total_battles", nullable = false)
    private Integer totalBattles = 0;

    @Column(name = "win_battles", nullable = false)
    private Integer winBattles = 0;

    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用 1-正常 2-邮箱未验证

    @Column(name = "email_verified", nullable = false)
    private Integer emailVerified = 0;

    @Column(name = "reset_token", length = 64)
    private String resetToken;

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "background_id", length = 50)
    private String backgroundId;

    @Column(name = "assistant_id")
    private Integer assistantId;

    @Column(name = "background_music", length = 255)
    private String backgroundMusic;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getTotalBattles() { return totalBattles; }
    public void setTotalBattles(Integer totalBattles) { this.totalBattles = totalBattles; }
    public Integer getWinBattles() { return winBattles; }
    public void setWinBattles(Integer winBattles) { this.winBattles = winBattles; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Integer emailVerified) { this.emailVerified = emailVerified; }
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public LocalDateTime getTokenExpiresAt() { return tokenExpiresAt; }
    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) { this.tokenExpiresAt = tokenExpiresAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getBackgroundId() { return backgroundId; }
    public void setBackgroundId(String backgroundId) { this.backgroundId = backgroundId; }
    public Integer getAssistantId() { return assistantId; }
    public void setAssistantId(Integer assistantId) { this.assistantId = assistantId; }
    public String getBackgroundMusic() { return backgroundMusic; }
    public void setBackgroundMusic(String backgroundMusic) { this.backgroundMusic = backgroundMusic; }
}