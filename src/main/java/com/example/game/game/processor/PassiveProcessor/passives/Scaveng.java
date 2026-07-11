package com.example.game.game.processor.PassiveProcessor.passives;

import com.example.game.entity.Role;
import com.example.game.game.Room;
import com.example.game.game.processor.PassiveProcessor.Passive;
import com.example.game.game.processor.PassiveProcessor.PassiveEffect;

@Passive("scaveng")
public class Scaveng implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
    //继承阵亡角色被动及技能（食腐者角色，有技能的角色不会再获得技能）
    room.addScavengRole(role);
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}