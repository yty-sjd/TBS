package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("sloth")
public class sloth implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //后置位存在角色时，后置位角色代替进行行动
        role.setBuff_replaceAction(1);
        //buff:role.getTeamId() + "阵营role.getRoleId() + "号位buff_replaceAction=1
        room.getStateProcessor().generateNewLine("buff:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位buff_replaceAction=1");
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}