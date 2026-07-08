package com.example.game.service.processor.PassiveProcessor.passives;

import com.example.game.service.Room;
import com.example.game.service.processor.PassiveProcessor.Passive;
import com.example.game.service.processor.PassiveProcessor.PassiveEffect;
import com.example.game.service.tbs_entity.Role;

@Passive("piece")
public class piece implements PassiveEffect {
    @Override
    public void apply(Role role, Room room) {
        room.getStateProcessor().generateNewLine("Passive:" + role.getTeamId() + "阵营" + role.getOrdinalSequence() + "号位使用被动" + "piece");
        //获得我方阵营其他角色的被动
        for (Role r : room.getRoles()[role.getTeamId()]) {
            if (r.getPassiveId() != null && r != role && !"piece".equals(r.getPassiveId())) {
                room.getPassiveExecutor().execute(role, r.getPassiveId(), room);
                room.getStateProcessor().generateNewLine("add_passive:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位获得"+role.getTeamId()+"阵营"+r.getOrdinalSequence()+"号位被动效果");
            }
        }
    }
    /**
     * 一个被动效果实例，应用于角色对象
     * @param role 角色对象
     */
}