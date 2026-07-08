package com.example.game.service.processor;

import com.example.game.service.Room;
import com.example.game.service.tbs_entity.Role;

/**
 * 角色位置交换处理器
 * 负责处理角色在阵营内或跨阵营的位置交换，同时维护位序编号的同步。
 */
public class PositionSwapProcessor {

    /** 阵营数量 */
    private static final int TEAM_COUNT = 2;
    /** 每阵营位置数量 */
    private static final int POSITION_COUNT = 5;

    /**
     * 同阵营内两个位置的角色交换
     * @param roles     双方阵营二维数组
     * @param teamId    阵营编号（0 或 1）
     * @param posA      位置 A（0-4）
     * @param posB      位置 B（0-4）
     * @return true=交换成功，false=参数无效
     */
    public void swapWithinTeam(Room room, int teamId, int posA, int posB) {

        Role temp = room.getRoles()[teamId][posA];
        room.getRoles()[teamId][posA] = room.getRoles()[teamId][posB];
        room.getRoles()[teamId][posB] = temp;

        updateOrdinalSequences(room.getRoles(), teamId);
        //PositionSwap:1阵营1号位与1阵营0号位交换位置
        room.getStateProcessor().generateNewLine("PositionSwap:" + teamId + "阵营" + posA + "号位与" + teamId + "阵营" + posB + "号位交换位置");
    }

    /**
     * 将阵亡角色移动到队尾，其后存活角色整体前移
     * 例：[A, D(死), B, C, E] → [A, B, C, E, D(死)]
     * @param roles   双方阵营二维数组
     * @param teamId  阵营编号
     * @param deadPos 死亡角色所在位置
     * @return true=移动成功，false=参数无效
     */
    public boolean swapDeadToBack(Room room, int teamId, int deadPos) {
        if (room == null || teamId < 0 || teamId >= TEAM_COUNT) {
            return false;
        }
        if (deadPos < 0 || deadPos >= POSITION_COUNT) {
            return false;
        }
        
        Role dead = room.getRoles()[teamId][deadPos];
        // deadPos 之后的角色全部前移一位
        for (int i = deadPos; i < POSITION_COUNT - 1; i++) {
            room.getRoles()[teamId][i] = room.getRoles()[teamId][i + 1];
            //PositionSwap:1阵营1号位与1阵营0号位交换位置
            room.getStateProcessor().generateNewLine("PositionSwap:" + teamId + "阵营" + i + "号位与" + teamId + "阵营" + (i+1) + "号位交换位置");
        }
        // 阵亡角色放到队尾
        room.getRoles()[teamId][POSITION_COUNT - 1] = dead;

        updateOrdinalSequences(room.getRoles(), teamId);
        return true;
    }

    // ==================== 内部工具方法 ====================

    /**
     * 同步更新某阵营所有角色的 OrdinalSequence 位序编号
     * OrdinalSequence = teamId * 5 + position
     */
    private void updateOrdinalSequences(Role[][] roles, int teamId) {
        for (int pos = 0; pos < POSITION_COUNT; pos++) {
            Role role = roles[teamId][pos];
            if (role != null) {
                role.setOrdinalSequence(pos);
                role.setTeamId(teamId);
            }
        }
    }
}