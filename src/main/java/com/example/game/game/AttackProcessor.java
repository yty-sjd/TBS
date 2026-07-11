package com.example.game.game;

import com.example.game.entity.Role;

/**
 * 攻击处理器
 * 负责单个攻击行为的全过程：
 * 1. 从敌方队伍选择攻击目标
 * 2. 计算攻击伤害
 * 3. 执行伤害
 */
public class AttackProcessor {

    public AttackProcessor() {
    }

    /**
     * 执行一次攻击
     * @param attacker 攻击者
     * @param enemyTeam 敌方队伍（一维数组）
      * @return 是否成功执行了攻击
     */
    public void attack(Role actor, Room room, Integer damage) {
        actor.setTempDamage(damage);//将攻击力暂存到角色对象中，供伤害处理器使用
        //处理角色攻击相关Buff
        if (!processAttackBuffs(actor, room)) {
            System.out.println(actor.getRoleName() + " 因Buff效果无法攻击！");
            return;
        }
        //引用受击处理器
        room.getDamageProcessor().DamageProcess(actor, room, damage);
        
    }
    /**
     * 处理攻击相关Buff
     * @param attacker 攻击者
     */
    private boolean processAttackBuffs(Role attacker, Room room) {
        //处理攻击相关Buff
        //是否不会攻击
        if(attacker.getBuff_notAttack() > 0){
            return false;
        }
        //是否每次造成伤害攻击力+1
        if(attacker.getBuff_damageAttackPlus() > 0){
            attacker.setAttack(attacker.getAttack() + attacker.getBuff_damageAttackPlus());
            ////change_attack:1阵营0号位role.attack
            room.getStateProcessor().generateNewLine("change_attack:" + attacker.getTeamId() + "阵营" + attacker.getOrdinalSequence() + "号位" + attacker.getAttack());
        }
        return true;
    }
}