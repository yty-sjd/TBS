package com.example.game.service.processor.SkillProcessor.skills;

import com.example.game.service.Room;
import com.example.game.service.processor.SkillProcessor.SkillAnnotation;
import com.example.game.service.tbs_entity.Role;
import com.example.game.service.tbs_entity.skill.Skill;

@SkillAnnotation(skillId = "Electric Mouse", skillName = "Electric Mouse", requiredMana = 3)
public class Electric_Mouse extends Skill {

    public Electric_Mouse(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        //skill:0阵营0号位使用技能Electric Mouse
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位Electric Mouse");
        //消耗技力
        //给予对面一层晕眩状态
        for(Role r : room.getRoles()[role.getEnemyTeamId()]){
            if(r.getBuff_notBecomeATargetOfSkill()==0){
                r.setBuff_dizziness(r.getBuff_dizziness()+1);//晕眩

                //阵营是否存在跟进buff
                if(room.getFollow_up()[role.getTeamId()][0]>0){
                    r.setBuff_dizziness(r.getBuff_dizziness()+1);//晕眩
                }
                //buff:1阵营0号位晕眩1
                room.getStateProcessor().generateNewLine("buff:"+r.getTeamId()+"阵营"+(r.getOrdinalSequence())+"号位buff_dizziness "+r.getBuff_dizziness());
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
