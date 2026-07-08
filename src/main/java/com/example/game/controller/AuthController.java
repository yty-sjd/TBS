package com.example.game.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户注册
     * POST /api/auth/register
     * { "username": "xxx", "password": "xxx", "email": "xxx" }
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户名不能为空"));
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "密码不能为空"));
        }

        Map<String, Object> result = authService.register(username, password, email);
        return ResponseEntity.ok(result);
    }

    /**
     * 用户登录
     * POST /api/auth/login
     * { "username": "xxx", "password": "xxx" }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户名不能为空"));
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "密码不能为空"));
        }

        String clientIp = getClientIp(request);
        Map<String, Object> result = authService.login(username, password, clientIp);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 更新用户背景
     * POST /api/auth/updateBackground
     * { "userId": 1, "backgroundId": "background2" }
     */
    @PostMapping("/updateBackground")
    public ResponseEntity<Map<String, Object>> updateBackground(@RequestBody Map<String, Object> body) {
        Integer userId = (Integer) body.get("userId");
        String backgroundId = (String) body.get("backgroundId");

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户ID不能为空"));
        }

        Map<String, Object> result = authService.updateBackground(userId, backgroundId);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新用户助理
     * POST /api/auth/updateAssistant
     * { "userId": 1, "assistantId": 30086009 }
     */
    @PostMapping("/updateAssistant")
    public ResponseEntity<Map<String, Object>> updateAssistant(@RequestBody Map<String, Object> body) {
        Integer userId = (Integer) body.get("userId");
        Integer assistantId = (Integer) body.get("assistantId");

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户ID不能为空"));
        }

        Map<String, Object> result = authService.updateAssistant(userId, assistantId);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新背景音乐（主题）
     * POST /api/auth/updateBackgroundMusic
     * { "userId": 1, "backgroundMusic": "2" }
     */
    @PostMapping("/updateBackgroundMusic")
    public ResponseEntity<Map<String, Object>> updateBackgroundMusic(@RequestBody Map<String, Object> body) {
        Integer userId = (Integer) body.get("userId");
        String backgroundMusic = (String) body.get("backgroundMusic");

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户ID不能为空"));
        }

        Map<String, Object> result = authService.updateBackgroundMusic(userId, backgroundMusic);
        return ResponseEntity.ok(result);
    }

    /**
     * 验证邮箱
     * POST /api/auth/verifyEmail
     */
    @PostMapping("/verifyEmail")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody Map<String, Object> body) {
        Integer userId = (Integer) body.get("userId");
        String email = (String) body.get("email");

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户ID不能为空"));
        }

        Map<String, Object> result = authService.verifyEmail(userId, email);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新账号信息
     * POST /api/auth/updateAccount
     */
    @PostMapping("/updateAccount")
    public ResponseEntity<Map<String, Object>> updateAccount(@RequestBody Map<String, Object> body) {
        Integer userId = (Integer) body.get("userId");
        String type = (String) body.get("type");
        String username = (String) body.get("username");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "用户ID不能为空"));
        }

        Map<String, Object> result = authService.updateAccount(userId, type, username, email, password);
        return ResponseEntity.ok(result);
    }

    /**
     * 重置密码（通过用户名+邮箱验证）
     * POST /api/auth/resetPassword
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        Map<String, Object> result = authService.resetPassword(username, email, password);
        return ResponseEntity.ok(result);
    }
}