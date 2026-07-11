package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("conceal")
public class Conceal implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        if(role.getOrdinalSequence() == room.getRoles()[role.getTeamId()].length-1){
            return;
        }
        //后置位队友4回合内不会成为攻击，效果对象
        //后面存在队友
        if(room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1] != null) {
            room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "conceal");
            room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].setBuff_notBecomeATargetOfAttack(4);
            //buff:role.getTeamId() + "阵营role.getOrdinalSequence() + "号位获得buff_notBecomeATargetOfAttack 4回合内不成为攻击目标
            room.getStateProcessor().generateNewLine("buff:" + role.getTeamId() + "阵营" + (role.getOrdinalSequence()+1) + "号位buff_notBecomeATargetOfAttack "+room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].getBuff_notBecomeATargetOfAttack());
            room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].setBuff_notBecomeATargetOfSkill(4);
            //buff:role.getTeamId() + "阵营role.getRoleId() + "号位buff_notBecomeATargetOfSkill 4回合内不成为效果目标
            room.getStateProcessor().generateNewLine("buff:" + role.getTeamId() + "阵营" + (role.getOrdinalSequence()+1) + "号位buff_notBecomeATargetOfSkill "+room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].getBuff_notBecomeATargetOfSkill());
        }
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}