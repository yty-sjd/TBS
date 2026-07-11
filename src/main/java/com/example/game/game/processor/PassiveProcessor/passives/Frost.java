package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("frost")
public class Frost implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //对方阵营1号位角色每次受到大于1的伤害时，再受到一次1点伤害 （状态）
        room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "frost");
        room.getFrost()[role.getEnemyTeamId()][0] = 1;
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}