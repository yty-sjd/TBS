package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("Stealing_Heaven")
public class stealing_heaven implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "stealing_heaven");
        //复制对位角色的被动效果
        //对位角色存在被动效果时，复制被动效果
        String enemyPassive = room.getRoles()[role.getEnemyTeamId()][role.getOrdinalSequence()].getPassiveId();
        if (!enemyPassive.isEmpty() && !"Stealing_Heaven".equals(enemyPassive)) {
            room.getPassiveExecutor().execute(role, enemyPassive, room);
            room.getStateProcessor().generateNewLine("add_passive:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位获得"+role.getEnemyTeamId()+"阵营"+role.getOrdinalSequence()+"号位被动效果");
        }
        //复制对位角色的技能
        //对位角色存在技能时，复制技能
        if(!room.getRoles()[role.getEnemyTeamId()][role.getOrdinalSequence()].getSkillId().isEmpty()){
            role.setSkillId(room.getRoles()[role.getEnemyTeamId()][role.getOrdinalSequence()].getSkillId());
            room.getStateProcessor().generateNewLine("add_skill:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位获得"+role.getEnemyTeamId()+"阵营"+role.getOrdinalSequence()+"号位技能");
        }
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}