package com.example.game.game.processor.SkillProcessor.skills;

import com.example.game.entity.Role;
import com.example.game.entity.Skill;
import com.example.game.game.Room;
import com.example.game.game.processor.SkillProcessor.SkillAnnotation;

@SkillAnnotation(skillId = "pharmacist_all", skillName = "pharmacist_all", requiredMana = 2)
public class  PharmacistAll extends Skill {

    public PharmacistAll(String skillName, String skillId, int requiredMana, Role role) {
        super(skillName, skillId, requiredMana, role);
    }

    @Override
    public void execute(Role role, Room room) {

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
        //skill:0阵营0号位使用技能pharmacist_all
        room.getStateProcessor().generateNewLine("skill:"+role.getTeamId()+"阵营"+(role.getOrdinalSequence())+"号位pharmacist_all");
        //"2sp		回复自己阵营全部角色2点生命值"
        //是否为群攻
        //2sp		回复自己阵营全部角色2点生命值
            //回复自己阵营全部角色2点生命值
            for(Role r : room.getRoles()[role.getTeamId()]){
                if(r.getHp()+2 > r.getHpMax() && r.getBuff_noHpLimit()==0 && r.getHp() > 0){
                    r.setHp(r.getHpMax());
                    //change_hp:1阵营0号位生命值9
                    room.getStateProcessor().generateNewLine("change_hp:"+r.getTeamId()+"阵营"+(r.getOrdinalSequence())+"号位"+r.getHp());
                }
                else if(r.getHp() > 0){
                    r.setHp(r.getHp() + 2);
                    //change_hp:1阵营0号位生命值11
                    room.getStateProcessor().generateNewLine("change_hp:"+r.getTeamId()+"阵营"+(r.getOrdinalSequence())+"号位"+r.getHp());
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

