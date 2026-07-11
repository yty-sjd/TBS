package com.example.game.game;

import com.example.game.entity.Role;

/**
 * 行动处理器
 * 协调单个角色的一次行动：
 * 1. 自动选择敌方目标
 * 2. 处理普通攻击伤害
 * 3. 释放技能（如果有）
 * 4. 触发被动效果
 */
public class ActionProcessor {

    public ActionProcessor() {
    }

    /**
     * 处理单个角色行动
     * @param actor 行动者（当前行动角色）
     * @param enemyTeam 敌方队伍数组
     * @return 是否成功执行了行动
     */
    public boolean processAction(Role actor, Room room) {

        //是否可以行动
        if(actor.getBuff_dizziness() == 0 && (actor.getBuff_replaceAction() == 0 || 
        (actor.getOrdinalSequence() == room.getRoles()[actor.getTeamId()].length-1)||//为最后的角色直接放行
        (actor.getBuff_replaceAction() == 1 && room.getRoles()[actor.getTeamId()][actor.getOrdinalSequence()+1].getHp() <= 0))){
            // 1. 执行攻击（）
            room.getAttackProcessor().attack(actor, room, actor.getAttack());

            //存在技能则回复技力
            if(!actor.getSkillId().isBlank())
            {
                actor.setCurrentMana(actor.getCurrentMana()+1);
                room.getStateProcessor().generateNewLine("change_currentMana:" + actor.getTeamId() + "阵营" + actor.getOrdinalSequence() + "号位"+actor.getCurrentMana());
            }

            // 2. 如果有技能且技力足够，释放技能
            if (actor.getSkillId() != null && !actor.getSkillId().isEmpty()) {
                room.getSkillExecutor().cast(actor, actor.getSkillId(), room);
            }
        }
        //是否被代替行动
        else if(actor.getBuff_replaceAction() == 1 && actor.getOrdinalSequence() != room.getRoles()[actor.getTeamId()].length-1 && room.getRoles()[actor.getTeamId()][actor.getOrdinalSequence()+1].getHp() > 0){
            room.getStateProcessor().generateNewLine("Action:" + actor.getTeamId() + "阵营" + (actor.getOrdinalSequence()+1) + "号位在行动");
            processAction(room.getRoles()[actor.getTeamId()][actor.getOrdinalSequence()+1], room);
        }

        //行动状态结束处理
        endAction(actor, room);

        //检查对面阵营该回合是否存在新阵亡角色
        room.getSurvivalChecker().checkNewSurvival(room, actor);

        return true;
    }

    //行动状态结束处理函数
    public void endAction(Role actor, Room room){
        //存在晕眩
        if(actor.getBuff_dizziness() > 0){
            actor.setBuff_dizziness(actor.getBuff_dizziness()-1);
            room.getStateProcessor().generateNewLine("buff:" + actor.getTeamId() + "阵营" + actor.getOrdinalSequence() + "号位buff_dizziness "+actor.getBuff_dizziness());
        }
        //存在剧毒
        if(actor.getBuff_poison() > 0){
            room.getDamageProcessor().DamageProcess(room.getPoison(), actor, room, actor.getBuff_poison());
            actor.setBuff_poison(actor.getBuff_poison()-1);
            room.getStateProcessor().generateNewLine("buff:" + actor.getTeamId() + "阵营" + actor.getOrdinalSequence() + "号位buff_poison "+actor.getBuff_poison());
        }
        //存在不能成为效果对象
        if(actor.getBuff_notBecomeATargetOfSkill() > 0){
            actor.setBuff_notBecomeATargetOfSkill(actor.getBuff_notBecomeATargetOfSkill()-1);
            room.getStateProcessor().generateNewLine("buff:" + actor.getTeamId() + "阵营" + actor.getOrdinalSequence() + "号位buff_notBecomeATargetOfSkill "+actor.getBuff_notBecomeATargetOfSkill());
        }
        //存在不能成为攻击对象
        if(actor.getBuff_notBecomeATargetOfAttack() > 0){
            actor.setBuff_notBecomeATargetOfAttack(actor.getBuff_notBecomeATargetOfAttack()-1);
            room.getStateProcessor().generateNewLine("buff:" + actor.getTeamId() + "阵营" + actor.getOrdinalSequence() + "号位buff_notBecomeATargetOfAttack "+actor.getBuff_notBecomeATargetOfAttack());
        }
        //存在放逐
        if(actor.getBuff_exile() > 0){  
            actor.setBuff_exile(actor.getBuff_exile()-1);   
            room.getStateProcessor().generateNewLine("buff:" + actor.getTeamId() + "阵营" + actor.getOrdinalSequence() + "号位buff_exile "+actor.getBuff_exile());
        }
    }

}