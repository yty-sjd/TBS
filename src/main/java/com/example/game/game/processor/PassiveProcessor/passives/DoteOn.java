package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("dote_on")
public class DoteOn implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //0号位获得代替受到的伤害buff
        room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "dote_on");
        room.getRoles()[role.getTeamId()][0].setBuff_damageReplace(1);
        room.getStateProcessor().generateNewLine("buff:" + role.getTeamId() + "阵营0号位buff_damageReplace 1");
        role.setBuff_damageReplacePlus(1);
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}