package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("pretentiousness")
public class pretentiousness implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //复制前置位角色被动，后置角色技能
        //如果前置位存在角色且前置位角色有被动效果
        room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "pretentiousness");
        if(role.getOrdinalSequence() > 0 && room.getRoles()[role.getTeamId()][role.getOrdinalSequence() - 1].getPassiveId() != null && !room.getRoles()[role.getTeamId()][role.getOrdinalSequence() - 1].getPassiveId().equals("pretentiousness")){
            //将前置位角色的被动效果设置为后置角色的被动效果
            room.getPassiveExecutor().execute(role, room.getRoles()[role.getTeamId()][role.getOrdinalSequence() - 1].getPassiveId(), room);
            room.getStateProcessor().generateNewLine("add_passive:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位获得"+role.getTeamId()+"阵营"+(role.getOrdinalSequence()-1)+"号位被动效果");
        }
        //如果后置位存在角色且后置位角色有技能,且自己没有技能
        if(role.getOrdinalSequence() < room.getRoles()[role.getTeamId()].length - 1 && room.getRoles()[role.getTeamId()][role.getOrdinalSequence() + 1].getSkillId() != null && role.getSkillId() == null){
            //将自己的技能设置为后置位角色的技能
            role.setSkillId(room.getRoles()[role.getTeamId()][role.getOrdinalSequence() + 1].getSkillId());
            room.getStateProcessor().generateNewLine("add_skill:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位获得"+role.getTeamId()+"阵营"+(role.getOrdinalSequence()+1)+"号位技能");
        }

    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}