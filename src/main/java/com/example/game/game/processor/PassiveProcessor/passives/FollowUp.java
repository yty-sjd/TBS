package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("follow_up")
public class FollowUp implements PassiveEffect {
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