package com.example.game.game.processor.PassiveProcessor;

import com.example.game.entity.Role;
import com.example.game.game.Room;

public interface PassiveEffect {
    void apply(Role role, Room room);
}
