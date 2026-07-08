package com.example.game.service.processor.SkillProcessor.skills;

import com.example.game.service.Room;
import com.example.game.service.processor.SkillProcessor.SkillAnnotation;
import com.example.game.service.tbs_entity.Role;
import com.example.game.service.tbs_entity.skill.Skill;

@SkillAnnotation(skillId = "fireball", skillName = "火球术", requiredMana = 30)
public class FireBall extends Skill {

    //该技能只用于参考，实际游戏中不会使用
    public FireBall(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        System.out.println(role.getRoleName() + " 释放了 " + skillName + "！造成 100 点伤害。");
    }
    /**
     * 一个技能效果实例，应用于角色对象
     * @param role 角色对象
     */
}
