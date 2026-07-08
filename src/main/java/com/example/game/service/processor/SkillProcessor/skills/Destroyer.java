package com.example.game.service.processor.SkillProcessor.skills;

import com.example.game.service.Room;
import com.example.game.service.processor.SkillProcessor.SkillAnnotation;
import com.example.game.service.tbs_entity.Role;
import com.example.game.service.tbs_entity.skill.Skill;

@SkillAnnotation(skillId = "Destroyer", skillName = "Destroyer", requiredMana = 2)
public class Destroyer extends Skill {

    public Destroyer(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        //skill:0阵营0号位使用技能Destroyer
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位Destroyer");
        //"2sp		造成1点伤害"
        //是否为群攻
        //不会成为攻击对象，不会成为效果对象，不会攻击,被放逐
        for(Role r : room.getRoles()[role.getEnemyTeamId()]){
            if(r.getBuff_notBecomeATargetOfSkill()==0){
                r.setBuff_notBecomeATargetOfSkill(r.getBuff_notBecomeATargetOfSkill()+1);
                r.setBuff_notBecomeATargetOfAttack(r.getBuff_notBecomeATargetOfAttack()+1);
                r.setBuff_dizziness(1);
                r.setBuff_exile(r.getBuff_exile()+1);
                room.getStateProcessor().generateNewLine("buff:" + r.getTeamId() + "阵营" + r.getOrdinalSequence() + "号位buff_exile "+r.getBuff_exile());
                if(role.getBuff_replaceSkill()!=1)
                {
                    break;
                }
            }
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
