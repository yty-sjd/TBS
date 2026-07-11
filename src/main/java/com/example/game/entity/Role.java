package com.example.game.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
/**
 * 角色实体类
 * 映射数据库表 roles
 */
@Entity
@Table(name = "roles")
public class Role {

    /**
     * 角色编号（主键，自增）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, length = 50, unique = true)
    private String roleName;

    /**
     * 当前生命值
     */
    @Column(name = "hp", nullable = false)
    private Integer hp = 0;

    /**
     * 生命上限
     */
    @Column(name = "hp_max", nullable = false)
    private Integer hpMax = 0;

    /**
     * 攻击力
     */
    @Column(name = "attack", nullable = false)
    private Integer attack = 0;

    //临时伤害数值
    private Integer tempDamage = 0;

    /**
     * 技能ID或代码
     */
    @Column(name = "skill_id", length = 50)
    private String skillId;

    /**
     * 被动技能ID或代码
     */
    @Column(name = "passive_id", length = 50)
    private String passiveId;

    /**
     * 技能描述
     */
    @Column(name = "skill_description", columnDefinition = "TEXT")
    private String skillDescription;

    /**
     * 被动技能描述
     */
    @Column(name = "passive_description", columnDefinition = "TEXT")
    private String passiveDescription;

    /**
     * 备注信息
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 立绘编号（资源标识）
     */
    @Column(name = "portrait_id", length = 50)
    private String portraitId;

    /**
     * 位序编号，用0-9表示，通过除以5决定阵营0或1，通过取模5决定角色在阵营中的位置（0-4）
     */
    private int OrdinalSequence;

    /**
     * 当前技力值
     */    
    private int currentMana = 0;

    /**
     * 阵营编号（0或1）
     */
    private Integer teamId;

    /**
     * 敌方阵营编号（运行时计算，不持久化）
     */
    private transient Integer enemyTeamId;

    //buff列表
    //攻击相关buff
    private Integer buff_notAttack = 0;//不会攻击
    private Integer buff_damageAttackPlus = 0;//每次造成伤害攻击力+1
    //受击相关buff
    private Integer buff_groupAttack = 0;//群攻
    private Integer buff_notBecomeATargetOfAttack = 0;//不成为攻击目标
    private Integer buff_damageReturn = 0;//每次受到伤害，返回一点伤害值
    private Integer buff_damageMinus = 0;//受到的伤害-1
    private Integer buff_damageReplace = 0;//被代替受到一点伤害
    private Integer buff_damageReplacePlus = 0;//每次代替受到一点伤害
    //行动相关buff
    private Integer buff_exile = 0;//放逐
    private Integer buff_replaceAction = 0;//代替行动
    private Integer buff_dizziness = 0;//晕眩
    //其他相关buff
    private Integer buff_layerPlus = 0;//buff层数+1
    private Integer buff_noHpLimit = 0;//无生命上限
    private Integer buff_notBecomeATargetOfSkill = 0;//不成为技能目标
    //技能相关buff
    private Integer buff_poison = 0;//剧毒
    private Integer buff_replaceSkill = 0;//技能对象改为对方阵营全部角色或我方阵营全部角色

    // 默认构造函数
    public Role() {
    }

    @PostLoad
    private void initBuffFields() {
        if (buff_notAttack == null) buff_notAttack = 0;
        if (buff_damageAttackPlus == null) buff_damageAttackPlus = 0;
        if (buff_groupAttack == null) buff_groupAttack = 0;
        if (buff_notBecomeATargetOfAttack == null) buff_notBecomeATargetOfAttack = 0;
        if (buff_damageReturn == null) buff_damageReturn = 0;
        if (buff_damageMinus == null) buff_damageMinus = 0;
        if (buff_damageReplace == null) buff_damageReplace = 0;
        if (buff_damageReplacePlus == null) buff_damageReplacePlus = 0;
        if (buff_replaceAction == null) buff_replaceAction = 0;
        if (buff_dizziness == null) buff_dizziness = 0;
        if (buff_layerPlus == null) buff_layerPlus = 0;
        if (buff_noHpLimit == null) buff_noHpLimit = 0;
        if (buff_notBecomeATargetOfSkill == null) buff_notBecomeATargetOfSkill = 0;
        if (buff_poison == null) buff_poison = 0;
        if (buff_replaceSkill == null) buff_replaceSkill = 0;
        if (buff_exile == null) buff_exile = 0;
        if (tempDamage == null) tempDamage = 0;
    }


    // Getter 和 Setter 方法
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getHpMax() {
        return hpMax;
    }

    public void setHpMax(Integer hpMax) {
        this.hpMax = hpMax;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getTempDamage() {
        return tempDamage;
    }

    public void setTempDamage(Integer tempDamage) {
        this.tempDamage = tempDamage;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getPassiveId() {
        return passiveId;
    }

    public void setPassiveId(String passiveId) {
        this.passiveId = passiveId;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public String getPassiveDescription() {
        return passiveDescription;
    }

    public void setPassiveDescription(String passiveDescription) {
        this.passiveDescription = passiveDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(String portraitId) {
        this.portraitId = portraitId;
    }

    public int getOrdinalSequence() {
        return OrdinalSequence;
    }

    public void setOrdinalSequence(int OrdinalSequence) {
        this.OrdinalSequence = OrdinalSequence;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
        this.enemyTeamId = (teamId == null) ? null : 1 - teamId;
    }

    public Integer getEnemyTeamId() {
        return enemyTeamId;
    }

    public void setEnemyTeamId(Integer enemyTeamId) {
        this.enemyTeamId = enemyTeamId;
    }

    public Integer getBuff_notBecomeATargetOfAttack() {
        return buff_notBecomeATargetOfAttack;
    }

    public Integer getBuff_notBecomeATargetOfSkill() {
        return buff_notBecomeATargetOfSkill;
    }

    public Integer getBuff_notAttack() {
        return buff_notAttack;
    }

    public Integer getBuff_groupAttack() {
        return buff_groupAttack;
    }

    public Integer getBuff_damageAttackPlus() {
        return buff_damageAttackPlus;
    }

    public Integer getBuff_damageReturn() {
        return buff_damageReturn;
    }

    public Integer getBuff_damageMinus() {
        return buff_damageMinus;
    }

    public Integer getBuff_damageReplace() {
        return buff_damageReplace;
    }

    public Integer getBuff_replaceAction() {
        return buff_replaceAction;
    }

    public Integer getBuff_dizziness() {
        return buff_dizziness;
    }

    public Integer getBuff_layerPlus() {
        return buff_layerPlus;
    }

    public Integer getBuff_noHpLimit() {
        return buff_noHpLimit;
    }

    public Integer getBuff_poison() {
        return buff_poison;
    }

    public void setBuff_notBecomeATargetOfAttack(Integer buff_notBecomeATargetOfAttack) {
        this.buff_notBecomeATargetOfAttack = buff_notBecomeATargetOfAttack;
    }

    public void setBuff_notBecomeATargetOfSkill(Integer buff_notBecomeATargetOfSkill) {
        this.buff_notBecomeATargetOfSkill = buff_notBecomeATargetOfSkill;
    }

    public void setBuff_notAttack(Integer buff_notAttack) {
        this.buff_notAttack = buff_notAttack;
    }

    public void setBuff_groupAttack(Integer buff_groupAttack) {
        this.buff_groupAttack = buff_groupAttack;
    }

    public void setBuff_damageAttackPlus(Integer buff_damageAttackPlus) {
        this.buff_damageAttackPlus = buff_damageAttackPlus;
    }

    public void setBuff_damageReturn(Integer buff_damageReturn) {
        this.buff_damageReturn = buff_damageReturn;
    }

    public void setBuff_damageMinus(Integer buff_damageMinus) {
        this.buff_damageMinus = buff_damageMinus;
    }

    public void setBuff_damageReplace(Integer buff_damageReplace) {
        this.buff_damageReplace = buff_damageReplace;
    }

    public void setBuff_replaceAction(Integer buff_replaceAction) {
        this.buff_replaceAction = buff_replaceAction;
    }

    public void setBuff_dizziness(Integer buff_dizziness) {
        this.buff_dizziness = buff_dizziness;
    }

    public void setBuff_layerPlus(Integer buff_layerPlus) {
        this.buff_layerPlus = buff_layerPlus;
    }

    public void setBuff_noHpLimit(Integer buff_noHpLimit) {
        this.buff_noHpLimit = buff_noHpLimit;
    }

    public void setBuff_poison(Integer buff_poison) {
        this.buff_poison = buff_poison;
    }
    
    public Integer getBuff_damageReplacePlus() {
        return buff_damageReplacePlus;
    }
    
    public void setBuff_damageReplacePlus(Integer buff_damageReplacePlus) {
        this.buff_damageReplacePlus = buff_damageReplacePlus;
    }

    public Integer getBuff_replaceSkill() {
        return buff_replaceSkill;
    }
    
    public void setBuff_replaceSkill(Integer buff_replaceSkill) {
        this.buff_replaceSkill = buff_replaceSkill;
    }

    public Integer getBuff_exile() {
        return buff_exile;
    }   
    
    public void setBuff_exile(Integer buff_exile) {
        this.buff_exile = buff_exile;
    }
}
