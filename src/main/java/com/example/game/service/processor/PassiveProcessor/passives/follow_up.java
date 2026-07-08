package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("follow_up")
public class follow_up implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "follow_up");
        room.getFollow_up()[role.getTeamId()][0] = 1;
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}