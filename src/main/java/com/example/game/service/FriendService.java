package com.example.game.service;


import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.game.Repository.FriendRepository;
import com.example.game.Repository.UserRepository;
import com.example.game.service.tbs_entity.Friend;
import com.example.game.service.tbs_entity.User;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> searchUser(String keyword, Integer myId) {
        var users = userRepository.findByUsernameContaining(keyword);
        var list = users.stream()
            .filter(u -> myId == null || !u.getId().equals(myId))
            .map(u -> {
                Friend rel = friendRepository.findRelation(myId, u.getId());
                String status = rel == null ? "none" : rel.getStatus() == 1 ? "friend" : rel.getStatus() == 0 ? "pending" : "rejected";
                return Map.of(
                    "id", u.getId(),
                    "username", u.getUsername(),
                    "assistantId", u.getAssistantId() != null ? u.getAssistantId() : Integer.valueOf(30086009),
                    "relation", status
                );
            })
            .toList();
        return Map.of("success", true, "users", list);
    }

    @Transactional
    public Map<String, Object> addFriend(Integer userId, Integer friendId) {
        if (userId.equals(friendId)) {
            return Map.of("success", false, "message", "不能添加自己为好友");
        }
        if (!userRepository.existsById(friendId)) {
            return Map.of("success", false, "message", "用户不存在");
        }
        Friend existing = friendRepository.findRelation(userId, friendId);
        if (existing != null) {
            return switch (existing.getStatus()) {
                case 0 -> Map.of("success", false, "message", "已发送申请，请等待对方确认");
                case 1 -> Map.of("success", false, "message", "已经是好友");
                default -> {
                    existing.setStatus(0);
                    friendRepository.save(existing);
                    yield Map.of("success", true, "message", "已重新发送申请");
                }
            };
        }
        friendRepository.save(new Friend(userId, friendId, 0));
        return Map.of("success", true, "message", "好友申请已发送");
    }

    @Transactional
    public Map<String, Object> acceptFriend(Integer friendId, Integer userId) {
        Friend f = friendRepository.findRelation(userId, friendId);
        if (f == null || f.getStatus() != 0 || !f.getFriendId().equals(userId)) {
            return Map.of("success", false, "message", "申请不存在");
        }
        f.setStatus(1);
        friendRepository.save(f);
        return Map.of("success", true, "message", "已添加好友");
    }

    @Transactional
    public Map<String, Object> rejectFriend(Integer friendId, Integer userId) {
        Friend f = friendRepository.findRelation(userId, friendId);
        if (f == null || f.getStatus() != 0 || !f.getFriendId().equals(userId)) {
            return Map.of("success", false, "message", "申请不存在");
        }
        f.setStatus(2);
        friendRepository.save(f);
        return Map.of("success", true, "message", "已拒绝");
    }

    @Transactional
    public Map<String, Object> deleteFriend(Integer userId, Integer friendId) {
        Friend f = friendRepository.findRelation(userId, friendId);
        if (f == null || f.getStatus() != 1) {
            return Map.of("success", false, "message", "不是好友");
        }
        friendRepository.delete(f);
        return Map.of("success", true, "message", "已删除好友");
    }

    public Map<String, Object> getFriendList(Integer userId) {
        var friends = friendRepository.findMyFriends(userId);
        var list = friends.stream().map(f -> {
            Integer otherId = f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId();
            User other = userRepository.findById(otherId).orElse(null);
            return Map.of(
                "friendId", otherId,
                "username", other != null ? other.getUsername() : "未知用户",
                "assistantId", other != null && other.getAssistantId() != null ? other.getAssistantId() : Integer.valueOf(30086009),
                "lastLoginAt", other != null && other.getLastLoginAt() != null ? other.getLastLoginAt().toString() : ""
            );
        }).toList();
        return Map.of("success", true, "friends", list);
    }

    public Map<String, Object> getRequests(Integer userId) {
        var requests = friendRepository.findByFriendIdAndStatus(userId, 0);
        var list = requests.stream().map(f -> {
            User sender = userRepository.findById(f.getUserId()).orElse(null);
            return Map.of(
                "id", f.getId(),
                "userId", f.getUserId(),
                "username", sender != null ? sender.getUsername() : "未知用户",
                "assistantId", sender != null && sender.getAssistantId() != null ? sender.getAssistantId() : Integer.valueOf(30086009)
            );
        }).toList();
        return Map.of("success", true, "requests", list);
    }
}