package com.example.game.service.processor.SkillProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SkillAnnotation {
    String skillId();        // 唯一标识，触发时用
    String skillName();      // 技能名
    int requiredMana();      // 发动所需技力
}
