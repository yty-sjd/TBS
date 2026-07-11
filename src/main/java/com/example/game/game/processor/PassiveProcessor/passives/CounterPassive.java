package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("counter")
public class CounterPassive implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        System.out.println("【反击】" + role.getRoleName() + " 反弹伤害！");
        // 简单模拟：回复 10 点生命代表反击回血
        role.setHp(role.getHp() + 10);
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}