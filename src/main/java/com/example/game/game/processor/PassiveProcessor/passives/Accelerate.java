package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("accelerate")
public class Accelerate implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //后置位角色技力+5
        if(role.getOrdinalSequence() != room.getRoles()[role.getTeamId()].length-1){
            //Passive:0阵营1号位使用被动accelerate
            room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "accelerate");
            room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].setCurrentMana(room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].getCurrentMana()+5);
            //change_currentMana:1阵营0号位技力5
            room.getStateProcessor().generateNewLine("change_currentMana:" + role.getTeamId() + "阵营" + (role.getOrdinalSequence()+1) + "号位" + room.getRoles()[role.getTeamId()][role.getOrdinalSequence()+1].getCurrentMana());
        }
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}