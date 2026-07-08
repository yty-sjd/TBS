package com.example.game.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.service.GameService;
import com.example.game.service.Room;
import com.example.game.service.tbs_entity.Role;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * 开始新游戏
     * POST /api/game/start
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start(@RequestBody(required = true) String roles) {
        String sessionId = gameService.startNewGame(roles);
        return ResponseEntity.ok(Map.of(
                "sessionId", sessionId
        ));
    }

    /**
     * 查看游戏房间状态（测试用：验证角色位置安排）
     * GET /api/game/room?sessionId=xxx
     */
    @GetMapping("/room")
    public ResponseEntity<Map<String, Object>> getRoom(@RequestParam("sessionId")  String sessionId) {
        Room room = gameService.getGame(sessionId);
        if (room == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "房间不存在"
            ));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", sessionId);

        List<List<Map<String, Object>>> teamA = new ArrayList<>();
        List<List<Map<String, Object>>> teamB = new ArrayList<>();

        Role[][] roles = room.getRoles();
        for (int j = 0; j < 5; j++) {
            if (roles[0][j] != null) {
                Role r = roles[0][j];
                teamA.add(List.of(Map.ofEntries(
                        Map.entry("position", j),
                        Map.entry("roleId", r.getRoleId()),
                        Map.entry("roleName", r.getRoleName()),
                        Map.entry("hp", r.getHp()),
                        Map.entry("hpMax", r.getHpMax()),
                        Map.entry("attack", r.getAttack()),
                        Map.entry("skillId", r.getSkillId() != null ? r.getSkillId() : "无"),
                        Map.entry("skillDescription", r.getSkillDescription() != null ? r.getSkillDescription() : "无"),
                        Map.entry("passiveId", r.getPassiveId() != null ? r.getPassiveId() : "无"),
                        Map.entry("passiveDescription", r.getPassiveDescription() != null ? r.getPassiveDescription() : "无"),
                        Map.entry("portraitId", r.getPortraitId() != null ? r.getPortraitId() : "")
                )));
            }
            if (roles[1][j] != null) {
                Role r = roles[1][j];
                teamB.add(List.of(Map.ofEntries(
                        Map.entry("position", j),
                        Map.entry("roleId", r.getRoleId()),
                        Map.entry("roleName", r.getRoleName()),
                        Map.entry("hp", r.getHp()),
                        Map.entry("hpMax", r.getHpMax()),
                        Map.entry("attack", r.getAttack()),
                        Map.entry("skillId", r.getSkillId() != null ? r.getSkillId() : "无"),
                        Map.entry("skillDescription", r.getSkillDescription() != null ? r.getSkillDescription() : "无"),
                        Map.entry("passiveId", r.getPassiveId() != null ? r.getPassiveId() : "无"),
                        Map.entry("passiveDescription", r.getPassiveDescription() != null ? r.getPassiveDescription() : "无"),
                        Map.entry("portraitId", r.getPortraitId() != null ? r.getPortraitId() : "")
                )));
            }
        }

        result.put("teamA", teamA);
        result.put("teamB", teamB);

        return ResponseEntity.ok(result);
    }

    /**
     * 查看当前战斗日志（不清空，可随时查看）
     * GET /api/game/log?sessionId=xxx
     */
    @GetMapping("/log")
    public ResponseEntity<String> getLog(@RequestParam("sessionId") String sessionId) {
        Room room = gameService.getGame(sessionId);
        if (room == null) {
            return ResponseEntity.status(404).body("Error: 房间不存在");
        }
        return ResponseEntity.ok(room.getStateProcessor().getCurrentLog());
    }

    /**
     * 上传战斗文本到前端
     * POST /api/game/turn?sessionId=xxx
     */
    @PostMapping("/turn")
    public ResponseEntity<String> doTurn(@RequestParam("sessionId") String sessionId) {
        Room room = gameService.getGame(sessionId);
        if (room == null) {
            return ResponseEntity.status(404).body("Error: 房间不存在");
        }
        String log = gameService.doTurn(sessionId);
        return ResponseEntity.ok(log);
    }

    /**
     * 创建游戏并一次性跑完整个战斗
     * POST /api/game/play
     */
    @PostMapping("/play")
    public ResponseEntity<Map<String, Object>> play(@RequestBody(required = true) String roles) {
        String sessionId = gameService.startAndPlay(roles);
        return ResponseEntity.ok(Map.of(
                "sessionId", sessionId,
                "gameOver", true
        ));
    }
}