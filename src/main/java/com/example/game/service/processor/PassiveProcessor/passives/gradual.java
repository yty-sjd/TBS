package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("gradual")
public class gradual implements PassiveEffect {
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