package com.example.game.entity;

import com.example.game.game.Room;

/**
 * 技能抽象基类
 */
public abstract class Skill {

    /**
     * 技能字段：技能名称
     */
    protected String skillName;

    /**
     * 技能ID
     */
    protected String skillId;

    /**
     * 发动要求技力：发动技能需要消耗的技力值
     */
    protected int requiredMana;

    /**
     * 发动角色对象
     */
    protected Role role;

    /**
     * 构造函数
     * @param skillName 技能名称
     * @param skillId 技能ID
     * @param requiredMana 发动所需技力
     * @param role 角色对象
     */
    public Skill(String skillName, String skillId, int requiredMana, Role role) {
        this.skillName = skillName;
        this.skillId = skillId;
        this.requiredMana = requiredMana;
        this.role = role;
    }

    /**
     * 技能执行方法
     */
       public abstract void execute(Role role, Room room);

}