package com.example.game.game.processor.SkillProcessor.skills;

import com.example.game.entity.Role;
import com.example.game.entity.Skill;
import com.example.game.game.Room;
import com.example.game.game.processor.SkillProcessor.SkillAnnotation;

@SkillAnnotation(skillId = "Archmage", skillName = "Archmage", requiredMana = 5)
public class Archmage extends Skill {

    public Archmage(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        //skill:0阵营0号位使用技能Archmage
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位Archmage");
        //"5sp		造成10点伤害"
        //是否为群攻
        if(role.getBuff_replaceSkill() == 1){
            //群攻
            Integer isGroupAttack = role.getBuff_groupAttack();
            role.setBuff_groupAttack(1);
            room.getAttackProcessor().attack(role, room, 8);
            role.setBuff_groupAttack(isGroupAttack);
        }
        else{
            //单攻
            room.getAttackProcessor().attack(role, room, 8);
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
