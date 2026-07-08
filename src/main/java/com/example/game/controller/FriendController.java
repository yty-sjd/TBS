package com.example.game.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.service.FriendService;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam("keyword") String keyword,
            @RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(friendService.searchUser(keyword, userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(friendService.addFriend(body.get("userId"), body.get("friendId")));
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> accept(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(friendService.acceptFriend(body.get("userId"), body.get("friendId")));
    }

    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> reject(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(friendService.rejectFriend(body.get("userId"), body.get("friendId")));
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(friendService.deleteFriend(body.get("userId"), body.get("friendId")));
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(friendService.getFriendList(userId));
    }

    @GetMapping("/requests")
    public ResponseEntity<Map<String, Object>> requests(@RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(friendService.getRequests(userId));
    }
}