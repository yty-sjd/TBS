package com.example.game.game.processor.SkillProcessor.skills;

import com.example.game.entity.Role;
import com.example.game.entity.Skill;
import com.example.game.game.Room;
import com.example.game.game.processor.SkillProcessor.SkillAnnotation;

@SkillAnnotation(skillId = "military_advisor", skillName = "military_advisor", requiredMana = 2)
public class MilitaryAdvisor extends Skill {

    public MilitaryAdvisor(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        //skill:0阵营0号位使用技能military_advisor
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位military_advisor");
        //"2sp		前置位角色与其前置位角色交换位置"
        if(role.getBuff_replaceSkill() == 1){
            //自己阵营的角色交换一次位置
            for(Role r : room.getRoles()[role.getTeamId()]){
                if(r.getOrdinalSequence() > 1 && r.getHp() > 0){
                    //交换位置
                    room.getPositionSwapProcessor().swapWithinTeam(room, r.getTeamId(), r.getOrdinalSequence() - 1, r.getOrdinalSequence()-2);
                }
            }
        }
        else if(role.getOrdinalSequence() > 1){
            //交换位置
            room.getPositionSwapProcessor().swapWithinTeam(room, role.getTeamId(), role.getOrdinalSequence() - 1, role.getOrdinalSequence()-2);
        }

        //消耗技力
        role.setCurrentMana(role.getCurrentMana() - requiredMana);
        //change_currentMana:0阵营0号位role.currentMana
        room.getStateProcessor().generateNewLine("change_currentMana:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位"+role.getCurrentMana());
    }
    /**
     * 一个技能效果实例，应用于角色对象
     * @param role 角色对象
     */
}
