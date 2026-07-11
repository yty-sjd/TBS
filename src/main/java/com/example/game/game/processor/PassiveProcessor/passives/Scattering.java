package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("scattering")
public class Scattering implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //该角色不会攻击，后置位角色攻击对象改为对方阵营全部角色
        //后置位存在角色且该角色存活
        if (role.getOrdinalSequence() < room.getRoles()[role.getTeamId()].length - 1
                && room.getRoles()[role.getTeamId()][role.getOrdinalSequence() + 1].getHp() > 0) {
            room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].setBuff_groupAttack(1);
            //buff:1阵营2号位buff_scaveng 4
            room.getStateProcessor().generateNewLine("buff:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence()+1)+"号位buff_groupAttack 1");
        }
        //该角色不会攻击
        role.setBuff_notAttack(1);
        //buff:1阵营2号位buff_notAttack 1
        room.getStateProcessor().generateNewLine("buff:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位buff_notAttack 1");
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}