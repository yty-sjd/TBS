package com.example.game.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.game.Repository.RoleRepository;



@Service
public class GameService {
    private final Map<String, Room> sessions = new ConcurrentHashMap<>();
    private final RoleRepository roleRepository;

    public GameService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public String startNewGame(String roles) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new Room(roles, roleRepository));
        return sessionId;
    }
    
    public Room getGame(String sessionId) {
        return sessions.get(sessionId);
    }

    // 对应房间游戏处理函数
    public String doTurn(String sessionId) {
        Room room = sessions.get(sessionId);
        if (room == null) {
            return "Error: 房间不存在";
        }
        return room.doTurn();
    }

    /**
     * 创建游戏并一次性跑完整个战斗，返回 sessionId
     */
    public String startAndPlay(String roles) {
        String sessionId = startNewGame(roles);
        Room room = sessions.get(sessionId);
        if (room != null) {
            room.runGame();
        }
        return sessionId;
    }
    // public SnakeGameState move(String sessionId, Direction direction) {
    //     Room game = sessions.get(sessionId);
    //     if (game == null) {
    //         // 返回一个错误状态
    //         return new SnakeGameState(null, null, null, 0, true,
    //                 "游戏不存在或已过期，请开始新游戏", 0, 0);
    //     }
    //     return game.move(direction);
    // }
/**
    // 可选：直接获取当前状态
    public SnakeGameState getState(String sessionId) {
        Room game = sessions.get(sessionId);
        if (game == null) {
            return new SnakeGameState(null, null, null, 0, true,
                    "游戏不存在", 0, 0);
        }
        // 可以添加一个 getState 方法，这里简单调用 move(null) 保持方向不变
        return game.move(null);
    }
        */
}