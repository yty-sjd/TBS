package com.example.game.game.processor.SkillProcessor.skills;

import com.example.game.entity.Role;
import com.example.game.entity.Skill;
import com.example.game.game.Room;
import com.example.game.game.processor.SkillProcessor.SkillAnnotation;

@SkillAnnotation(skillId = "viper", skillName = "viper", requiredMana = 2)
public class viper extends Skill {

    public viper(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        //skill:0阵营0号位使用技能viper
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位viper");
        //"2sp		给予对面两层剧毒"
        //是否为群攻
        //给予对面两层剧毒
        for(Role r : room.getRoles()[role.getEnemyTeamId()]){
            if(r.getBuff_notBecomeATargetOfSkill()==0){
                r.setBuff_poison(r.getBuff_poison()+2);//剧毒
                //阵营是否存在跟进buff
                if(room.getFollow_up()[role.getTeamId()][0]>0){
                    r.setBuff_poison(r.getBuff_poison()+1);//剧毒
                }
                room.getStateProcessor().generateNewLine("buff:" + r.getTeamId() + "阵营" + r.getOrdinalSequence() + "号位buff_poison "+r.getBuff_poison());
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
