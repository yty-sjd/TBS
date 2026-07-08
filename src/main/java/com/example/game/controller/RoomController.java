package com.example.game.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.service.RoomService;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/invite")
    public ResponseEntity<Map<String, Object>> invite(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(roomService.invite(body.get("fromUserId"), body.get("toUserId")));
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> accept(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(roomService.accept(body.get("invitationId"), body.get("userId")));
    }

    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> reject(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(roomService.reject(body.get("invitationId"), body.get("userId")));
    }

    @GetMapping("/invitations")
    public ResponseEntity<Map<String, Object>> invitations(@RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(roomService.getPendingInvitations(userId));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status(@RequestParam("roomId") Integer roomId) {
        return ResponseEntity.ok(roomService.checkRoomStatus(roomId));
    }

    @PostMapping("/ready")
    public ResponseEntity<Map<String, Object>> ready(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(roomService.setReady(
            (Integer) body.get("roomId"),
            (Integer) body.get("userId"),
            (Boolean) body.get("isHost"),
            (Boolean) body.get("ready")
        ));
    }

    @PostMapping("/leave")
    public ResponseEntity<Map<String, Object>> leave(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(roomService.leave(body.get("roomId"), body.get("userId")));
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(roomService.startGame(body.get("roomId"), body.get("userId")));
    }
}