package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("Magic")
public class Magic implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //受到的伤害-1
        role.setBuff_damageMinus(1);
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}