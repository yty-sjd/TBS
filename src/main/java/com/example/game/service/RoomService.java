package com.example.game.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.game.entity.RoomInvitation;
import com.example.game.entity.User;
import com.example.game.repository.RoomInvitationRepository;
import com.example.game.repository.UserRepository;

@Service
public class RoomService {

    private final RoomInvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private int nextRoomId = 1;

    public RoomService(RoomInvitationRepository invitationRepository, UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, Object> startGame(Integer roomId, Integer userId) {
        var inv = invitationRepository.findAllByRoomIdAndStatus(roomId, 1).stream().findFirst().orElse(null);
        if (inv == null) {
            return Map.of("success", false, "message", "房间不存在");
        }
        if (!inv.getFromUser().equals(userId)) {
            return Map.of("success", false, "message", "只有房主可以开始");
        }
        inv.setStatus(4);
        invitationRepository.save(inv);
        return Map.of("success", true, "message", "游戏已开始");
    }

    @Transactional
    public Map<String, Object> invite(Integer fromUserId, Integer toUserId) {
        if (fromUserId.equals(toUserId)) {
            return Map.of("success", false, "message", "不能邀请自己");
        }
        if (!userRepository.existsById(toUserId)) {
            return Map.of("success", false, "message", "用户不存在");
        }
        var existing = invitationRepository.findPendingInvitations(fromUserId, toUserId);
        if (!existing.isEmpty()) {
            return Map.of("success", false, "message", "已有待处理的邀请");
        }
        int roomId = nextRoomId++;
        invitationRepository.save(new RoomInvitation(roomId, fromUserId, toUserId, 0));
        return Map.of("success", true, "message", "邀请已发送", "roomId", roomId);
    }

    @Transactional
    public Map<String, Object> accept(Integer invitationId, Integer userId) {
        var inv = invitationRepository.findById(invitationId).orElse(null);
        if (inv == null || !inv.getToUser().equals(userId) || inv.getStatus() != 0) {
            return Map.of("success", false, "message", "邀请不存在或已过期");
        }
        inv.setStatus(1);
        invitationRepository.save(inv);
        return Map.of("success", true, "message", "已接受邀请", "roomId", inv.getRoomId());
    }

    @Transactional
    public Map<String, Object> reject(Integer invitationId, Integer userId) {
        var inv = invitationRepository.findById(invitationId).orElse(null);
        if (inv == null || !inv.getToUser().equals(userId) || inv.getStatus() != 0) {
            return Map.of("success", false, "message", "邀请不存在或已过期");
        }
        inv.setStatus(2);
        invitationRepository.save(inv);
        return Map.of("success", true, "message", "已拒绝邀请");
    }

    public Map<String, Object> getPendingInvitations(Integer userId) {
        var list = invitationRepository.findByToUserAndStatus(userId, 0);
        var result = list.stream().map(inv -> {
            User sender = userRepository.findById(inv.getFromUser()).orElse(null);
            return Map.of(
                "id", inv.getId(),
                "fromUserId", inv.getFromUser(),
                "fromUsername", sender != null ? sender.getUsername() : "未知用户",
                "assistantId", sender != null && sender.getAssistantId() != null ? sender.getAssistantId() : Integer.valueOf(30086009),
                "roomId", inv.getRoomId()
            );
        }).toList();
        return Map.of("success", true, "invitations", result);
    }

    public Map<String, Object> checkRoomStatus(Integer roomId) {
        var left = invitationRepository.findAllByRoomIdAndStatus(roomId, 3).stream().findFirst().orElse(null);
        if (left != null) {
            return Map.of("success", true, "joined", true, "left", true);
        }
        var started = invitationRepository.findAllByRoomIdAndStatus(roomId, 4).stream().findFirst().orElse(null);
        if (started != null) {
            return Map.of("success", true, "joined", true, "started", true);
        }
        var accepted = invitationRepository.findAllByRoomIdAndStatus(roomId, 1).stream().findFirst().orElse(null);
        if (accepted != null) {
            User guest = userRepository.findById(accepted.getToUser()).orElse(null);
            return Map.of("success", true, "joined", true,
                "guestId", accepted.getToUser(),
                "guestUsername", guest != null ? guest.getUsername() : "未知用户",
                "guestAssistantId", guest != null && guest.getAssistantId() != null ? guest.getAssistantId() : Integer.valueOf(30086009),
                "hostReady", accepted.getHostReady() == 1,
                "guestReady", accepted.getGuestReady() == 1
            );
        }
        return Map.of("success", true, "joined", false, "hostReady", false, "guestReady", false);
    }

    @Transactional
    public Map<String, Object> leave(Integer roomId, Integer userId) {
        var inv = invitationRepository.findAllByRoomIdAndStatus(roomId, 1).stream().findFirst().orElse(null);
        if (inv == null) {
            return Map.of("success", false, "message", "房间不存在");
        }
        inv.setStatus(3);
        invitationRepository.save(inv);
        return Map.of("success", true);
    }

    @Transactional
    public Map<String, Object> setReady(Integer roomId, Integer userId, Boolean isHost, Boolean ready) {
        var inv = invitationRepository.findAllByRoomIdAndStatus(roomId, 1).stream().findFirst().orElse(null);
        if (inv == null) {
            return Map.of("success", false, "message", "房间不存在");
        }
        if (isHost) {
            inv.setHostReady(ready ? 1 : 0);
        } else {
            inv.setGuestReady(ready ? 1 : 0);
        }
        invitationRepository.save(inv);
        return Map.of("success", true);
    }

    @Transactional
    @Scheduled(fixedRate = 600000)
    public void cleanExpiredRooms() {
        var cutoff = LocalDateTime.now().minusHours(1);
        invitationRepository.deleteByCreatedAtBefore(cutoff);
    }
}