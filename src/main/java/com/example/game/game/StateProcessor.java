package com.example.game.game;

/**
 * 操作步骤文本生成器
 * 用于逐步记录游戏中的关键行为（攻击、受击、攻击力变化、结束），
 * 并将累积的文本一次性上传到前端（此处以返回字符串模拟）。
 */
public class StateProcessor {
    // ---------- 变量 ----------
    /** 累积的操作文本 */
    private StringBuilder textLog = new StringBuilder();

    // ---------- 构造方法 ----------
    public StateProcessor() {}

    // ---------- 文本生成函数 ----------

    /**
     * 攻击操作文本生成函数
     * @param attacker 攻击对象
     * @param defender 受击对象
     * @param damage   造成的伤害数值
     */
    public void generateAttackAction(String attacker, String defender, int damage) {
        String msg = String.format("Attack:%s %s %d", attacker, defender, damage);
        textLog.append(msg).append("\n");
    }

    /**
     * 受击操作文本生成函数
     * @param defender 受击对象
     * @param damage   受到的伤害数值
     */
    public void generateDefenseAction(String defender, int damage) {
        String msg = String.format("Defense:%s %d", defender, damage);
        textLog.append(msg).append("\n");
    }

    /**
     * 攻击力改变文本生成函数
     * @param target    攻击力改变的对象
     * @param newAttack 变化后的攻击力数值
     */
    public void generatePowerChange(String target, int newAttack) {
        String msg = String.format("PowerChange:%s %d", target, newAttack);
        textLog.append(msg).append("\n");
    }

    /**
     * 游戏结束文本生成函数
     */
    public void generateGameOver() {
        textLog.append("GameOver\n");
    }

    //加入文本加换行
    public void generateNewLine(String msg) {
        textLog.append(msg).append("\n");
    }

    // ---------- 上传函数 ----------

    /**
     * 上传文本函数（模拟发送到前端）
     * @return 累积的操作步骤文本，上传后内部缓存清空
     */
    public String uploadText() {
        String text = textLog.toString();
        textLog.setLength(0);   // 清空缓存，准备下一轮记录
        return text;
    }

    /**
     * 直接打印上传内容（用于演示）
     */
    public void uploadAndPrint() {
        System.out.println("----- 上传到前端的文本 -----");
        System.out.print(textLog.toString());
        textLog.setLength(0);
    }

    // ---------- 可选：清空当前记录 ----------
    public void clearLog() {
        textLog.setLength(0);
    }

    // ---------- 可选：获取当前累积文本（不清空） ----------
    public String getCurrentLog() {
        return textLog.toString();
    }

    public StringBuilder getTextLog() {
        return textLog;
    }

    public void setTextLog(StringBuilder textLog) {
        this.textLog = textLog;
    }
}
