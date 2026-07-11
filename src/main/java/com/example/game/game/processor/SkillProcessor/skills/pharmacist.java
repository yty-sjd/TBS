package com.example.game.game.processor.SkillProcessor.skills;

import com.example.game.entity.Role;
import com.example.game.entity.Skill;
import com.example.game.game.Room;
import com.example.game.game.processor.SkillProcessor.SkillAnnotation;

@SkillAnnotation(skillId = "pharmacist", skillName = "pharmacist", requiredMana = 2)
public class Pharmacist extends Skill {

    public Pharmacist(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {
        Role caster = role;
        if(role.getBuff_replaceSkill() == 1){
            //检查自己阵营是否有角色生命值未满
            int count=0;
            for(Role r : room.getRoles()[role.getTeamId()]){
                if((r.getHp() < r.getHpMax() && r.getHp() > 0)||r.getBuff_noHpLimit() > 0){
                    role=r;
                    break;
                }
                count++;
                if(count == 5){
                    return;
                }
            }
            //skill:0阵营0号位使用技能pharmacist
            room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位pharmacist");
            //回复自己阵营全部角色4点生命值
            for(Role r : room.getRoles()[role.getTeamId()]){
                if(r.getHp()+4 > r.getHpMax() && r.getBuff_noHpLimit()==0 && r.getHp() > 0){
                    r.setHp(r.getHpMax());
                    //change_hp:1阵营0号位生命值9
                    room.getStateProcessor().generateNewLine("change_hp:"+r.getTeamId()+"阵营"+(r.getOrdinalSequence())+"号位"+r.getHp());
                }
                else if(r.getHp() > 0){
                    r.setHp(r.getHp() + 4);
                    //change_hp:1阵营0号位生命值13
                    room.getStateProcessor().generateNewLine("change_hp:"+r.getTeamId()+"阵营"+(r.getOrdinalSequence())+"号位"+r.getHp());
                }
            }
            //消耗技力
            caster.setCurrentMana(caster.getCurrentMana() - requiredMana);
            //change_currentMana:0阵营0号位role.currentMana
            room.getStateProcessor().generateNewLine("change_currentMana:" + caster.getTeamId() + "阵营" + caster.getOrdinalSequence() + "号位"+caster.getCurrentMana());
        }
        else if(role.getOrdinalSequence() > 0 && (!room.getRoles()[role.getTeamId()][role.getOrdinalSequence()-1].getHp().equals(room.getRoles()[role.getTeamId()][role.getOrdinalSequence()-1].getHpMax()) || room.getRoles()[role.getTeamId()][role.getOrdinalSequence()-1].getBuff_noHpLimit() > 0)){
            //skill:0阵营0号位使用技能pharmacist
            room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位pharmacist");
            role=room.getRoles()[role.getTeamId()][role.getOrdinalSequence()-1];
            //回复前置位角色4点生命值
            if(role.getHp()+4 > role.getHpMax() && role.getBuff_noHpLimit()==0 && role.getHp() > 0){
                role.setHp(role.getHpMax());
                //change_hp:1阵营0号位生命值9
                room.getStateProcessor().generateNewLine("change_hp:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位"+role.getHp());
            }
            else if(role.getHp() > 0){
                role.setHp(role.getHp() + 4);
                //change_hp:1阵营0号位生命值13
                room.getStateProcessor().generateNewLine("change_hp:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位"+role.getHp());
            }
            //消耗技力
            caster.setCurrentMana(caster.getCurrentMana() - requiredMana);
            //change_currentMana:0阵营0号位role.currentMana
            room.getStateProcessor().generateNewLine("change_currentMana:" + caster.getTeamId() + "阵营" + caster.getOrdinalSequence() + "号位"+caster.getCurrentMana());
        }
    }
    /**
     * 一个技能效果实例，应用于角色对象
     * @param role 角色对象
     */
}