package com.example.game.game;

import java.util.ArrayList;

import com.example.game.entity.Role;

public class SurvivalChecker {

    public SurvivalChecker() {}

    //阵亡处理器
    public void checkSurvival(Role role, Room room) {
        if (role != null && role.getHp() <= 0) {
            role.setHp(0);
            //阵营存活角色数量减1
            room.getSurvivalCount()[role.getTeamId()][0]--;
            //checkSurvival:1阵营0号位阵亡
            room.getStateProcessor().generateNewLine("checkSurvival:"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位阵亡");
            if (!checkSurvival(room, role)) {
                room.setGameOver(true);
                //游戏结束,对面阵营获胜
                room.getStateProcessor().generateNewLine("Game Over:"+role.getEnemyTeamId()+"阵营获胜");
            }
            else{
                //处理对行动指针造成的影响，如果阵亡角色的序号小于行动指针，则将行动指针减1
                if(role.getOrdinalSequence() < room.getActionPointer()[role.getTeamId()][0]){
                    room.getActionPointer()[role.getTeamId()][0]--;
                }
                //检查是否存在食腐者角色，如果有，读取一遍食腐者角色列表
                for (Role scavengRole : new ArrayList<>(room.getScavengRoles())) {
                    //检查食腐者角色是否存活
                    if(scavengRole.getHp() <= 0){
                        continue;
                    }
                    //检查食腐者是否有技能且阵亡角色是否有技能
                    if(scavengRole.getSkillId() == null && role.getSkillId() != null){
                        //如果没有技能，将该角色的技能设置为阵亡角色的技能
                        scavengRole.setSkillId(role.getSkillId());
                        room.getStateProcessor().generateNewLine("add_skill:"+scavengRole.getTeamId()+"阵营"+scavengRole.getOrdinalSequence()+"号位获得"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位技能");
                    }
                    //检查阵亡角色是否有被动效果，且不是食腐者角色
                    if(role.getPassiveId() != null && !role.getPassiveId().equals("scaveng")){
                        //食腐者角色执行一次该角色的被动效果
                        room.getPassiveExecutor().execute(scavengRole, role.getPassiveId(), room);
                        room.getStateProcessor().generateNewLine("add_passive:"+scavengRole.getTeamId()+"阵营"+scavengRole.getOrdinalSequence()+"号位获得"+role.getTeamId()+"阵营"+role.getOrdinalSequence()+"号位被动效果");
                    }
                }
                //如果该阵营还有存活的角色，将阵亡角色移动到该阵营的最右侧，后面的角色向左移动
                room.getPositionSwapProcessor().swapDeadToBack(room, role.getTeamId(), role.getOrdinalSequence());
            }
        }
    }

    //检查一个阵营的所有角色是否存活
    public boolean checkSurvival(Room room, Role defender) {
        for (Role role : room.getRoles()[defender.getTeamId()]) {
            if (role != null && role.getHp() > 0) {
                return true;
            }
        }
        return false;
    }

    //回合结束检查对方阵营是否存在新阵亡角色
    public void checkNewSurvival(Room room, Role attacker){
        //检查对方阵营是否存在新阵亡角色
        for (int i = 0; i < room.getSurvivalCount()[attacker.getEnemyTeamId()][0]; i++) {
            if(room.getRoles()[attacker.getEnemyTeamId()][i].getHp() <= 0){
                checkSurvival(room.getRoles()[attacker.getEnemyTeamId()][i], room);
            }
        }
    }
}