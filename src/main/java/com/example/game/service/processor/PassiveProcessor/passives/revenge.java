package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("revenge")
public class revenge implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        //每次受到伤害反伤1点伤害
        role.setBuff_damageReturn(role.getBuff_damageReturn() + 1);
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}