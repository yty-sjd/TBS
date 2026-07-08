package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("radiation")
public class radiation implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //后置位角色技能对象改为对方阵营全部角色或我方阵营全部角色
        //后置位存在角色且该角色存活
        if(role.getOrdinalSequence() !=room.getRoles()[role.getTeamId()].length-1 && room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].getHp() > 0){
            room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].setBuff_replaceSkill(1);
            //buff:1阵营2号位buff_replaceSkill 1
            room.getStateProcessor().generateNewLine("buff:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence()+1)+"号位buff_replaceSkill 1");
        }
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}