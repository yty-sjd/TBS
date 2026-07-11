package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("gradual")
public class Gradual implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //每次攻击后攻击力+1
        role.setBuff_damageAttackPlus(role.getBuff_damageAttackPlus()+1);
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}