package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("change")
public class Change implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //对位敌人与1号位敌人交换位置
        if(room.getRoles()[role.getEnemyTeamId()][role.getOrdinalSequence()] != null && role.getOrdinalSequence() != 0){
            //Passive:0阵营1号位使用被动change
            room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "change");
            //交换位置
            room.getPositionSwapProcessor().swapWithinTeam(room, role.getEnemyTeamId(), 0, role.getOrdinalSequence());
        }
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}