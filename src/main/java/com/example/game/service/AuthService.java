package com.example.game.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.game.entity.User;
import com.example.game.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 用户注册
     */
    public Map<String, Object> register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            return Map.of("success", false, "message", "用户名已存在");
        }
        // 检查邮箱是否已存在
        if (email != null && !email.isEmpty() && userRepository.existsByEmail(email)) {
            return Map.of("success", false, "message", "邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        userRepository.save(user);

        return Map.of("success", true, "message", "注册成功");
    }

    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password, String clientIp) {
        // 查找用户
        var optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户名或密码错误");
        }

        User user = optUser.get();

        // 检查账号状态
        if (user.getStatus() == 0) {
            return Map.of("success", false, "message", "账号已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Map.of("success", false, "message", "用户名或密码错误");
        }

        // 更新最后登录信息
        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(clientIp);
        userRepository.save(user);

        // 返回用户信息（不返回密码）
        return Map.of(
            "success", true,
            "message", "登录成功",
            "token", UUID.randomUUID().toString(), // 简单 token，这里简化处理
            "user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail() != null ? user.getEmail() : "",
                "totalBattles", user.getTotalBattles(),
                "winBattles", user.getWinBattles(),
                "backgroundId", user.getBackgroundId() != null ? user.getBackgroundId() : "",
                "assistantId", user.getAssistantId() != null ? user.getAssistantId() : Integer.valueOf(30086009),
                "background_music", user.getBackgroundMusic() != null ? user.getBackgroundMusic() : "1"
            )
        );
    }

    /**
     * 更新用户背景
     */
    public Map<String, Object> updateBackground(Integer userId, String backgroundId) {
        var optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户不存在");
        }
        User user = optUser.get();
        user.setBackgroundId(backgroundId);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Map.of("success", true, "message", "背景已更新");
    }

    /**
     * 更新助理
     */
    public Map<String, Object> updateAssistant(Integer userId, Integer assistantId) {
        var optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户不存在");
        }
        User user = optUser.get();
        user.setAssistantId(assistantId);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Map.of("success", true, "message", "助理已更新");
    }

    /**
     * 更新背景音乐
     */
    public Map<String, Object> updateBackgroundMusic(Integer userId, String backgroundMusic) {
        var optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户不存在");
        }
        User user = optUser.get();
        user.setBackgroundMusic(backgroundMusic);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Map.of("success", true, "message", "主题已更换");
    }

    /**
     * 验证邮箱
     */
    public Map<String, Object> verifyEmail(Integer userId, String email) {
        var optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户不存在");
        }
        User user = optUser.get();
        if (!user.getEmail().equals(email)) {
            return Map.of("success", false, "message", "邮箱验证失败");
        }
        return Map.of("success", true, "message", "验证通过");
    }

    /**
     * 更新账号信息
     */
    public Map<String, Object> updateAccount(Integer userId, String type, String username, String email, String password) {
        var optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户不存在");
        }
        User user = optUser.get();
        switch (type) {
            case "username" -> {
                if (username == null || username.isBlank()) {
                    return Map.of("success", false, "message", "用户名不能为空");
                }
                user.setUsername(username);
            }
            case "email" -> {
                if (email == null || email.isBlank()) {
                    return Map.of("success", false, "message", "邮箱不能为空");
                }
                user.setEmail(email);
            }
            case "password" -> {
                if (password == null || password.isBlank()) {
                    return Map.of("success", false, "message", "密码不能为空");
                }
                user.setPassword(passwordEncoder.encode(password));
            }
            default -> { return Map.of("success", false, "message", "未知操作类型"); }
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Map.of("success", true, "message", "修改成功");
    }

    /**
     * 重置密码（通过用户名+邮箱验证）
     */
    @Transactional
    public Map<String, Object> resetPassword(String username, String email, String password) {
        var optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return Map.of("success", false, "message", "用户不存在");
        }
        User user = optUser.get();
        if (!user.getEmail().equals(email)) {
            return Map.of("success", false, "message", "邮箱验证失败");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Map.of("success", true, "message", "密码修改成功");
    }
}