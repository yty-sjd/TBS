package com.example.game.service.processor.PassiveProcessor;

import com.example.game.service.Room;
import com.example.game.service.tbs_entity.Role;

public interface PassiveEffect {
    void apply(Role role, Room room);
}
