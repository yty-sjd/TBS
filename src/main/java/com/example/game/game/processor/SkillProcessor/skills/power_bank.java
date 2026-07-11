package com.example.game.game.processor.SkillProcessor.skills;

import com.example.game.entity.Role;
import com.example.game.entity.Skill;
import com.example.game.game.Room;
import com.example.game.game.processor.SkillProcessor.SkillAnnotation;

@SkillAnnotation(skillId = "power bank", skillName = "power bank", requiredMana = 2)
public class power_bank extends Skill {

    public power_bank(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        //skill:0阵营0号位使用技能power_bank
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位power_bank");
        //给予队友同阵营角色两点技力
        for(Role r : room.getRoles()[role.getTeamId()]){
            if(r == role){
                continue;
            }
            r.setCurrentMana(r.getCurrentMana()+2);
            //change_currentMana:0阵营0号位role.currentMana
            room.getStateProcessor().generateNewLine("change_currentMana:" + r.getTeamId() + "阵营" + r.getOrdinalSequence() + "号位"+r.getCurrentMana());
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
