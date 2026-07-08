package com.example.game.service.processor.SkillProcessor;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.example.game.service.Room;
import com.example.game.service.tbs_entity.Role;
import com.example.game.service.tbs_entity.skill.Skill;

public class SkillExecutor {
    // skillId -> 技能子类 Class 对象
    private final Map<String, Class<? extends Skill>> skillClassMap = new HashMap<>();

    /**
     * 构造时自动扫描指定包下所有带 @SkillAnnotation 的技能子类
     * @param packageName 技能类所在包名，如 "com.example.game.service.processor.SkillProcessor.skills"
     */
    public SkillExecutor(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("jar".equals(resource.getProtocol())) {
                    JarURLConnection connection = (JarURLConnection) resource.openConnection();
                    JarFile jarFile = connection.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path) && name.endsWith(".class")) {
                            String className = name.substring(0, name.length() - 6).replace('/', '.');
                            registerSkillClass(className);
                        }
                    }
                } else {
                    String decodedPath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8);
                    File directory = new File(decodedPath);
                    if (directory.exists()) {
                        scanDirectory(directory, packageName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerSkillClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(SkillAnnotation.class) &&
                    Skill.class.isAssignableFrom(clazz)) {
                SkillAnnotation anno = clazz.getAnnotation(SkillAnnotation.class);
                @SuppressWarnings("unchecked")
                Class<? extends Skill> skillClass = (Class<? extends Skill>) clazz;
                skillClassMap.put(anno.skillId(), skillClass);
                System.out.println("自动注册技能：" + anno.skillId() + " -> " + className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanDirectory(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." +
                        file.getName().substring(0, file.getName().length() - 6);
                registerSkillClass(className);
            }
        }
    }

    /**
     * 根据 skillId 释放技能
     * @param role      释放技能的角色
     * @param skillId   技能ID
     */
    public void cast(Role role, String skillId, Room room) {
        Class<? extends Skill> clazz = skillClassMap.get(skillId);
        if (clazz == null) {
            System.out.println("技能不存在：" + skillId);
            return;
        }

        SkillAnnotation anno = clazz.getAnnotation(SkillAnnotation.class);
        // 检查技力是否足够
        if (role.getCurrentMana() < anno.requiredMana()) {
            System.out.println("技力不足！需要 " + anno.requiredMana() + "，当前 " + role.getCurrentMana());
            return;
        }

        try {
            // 使用你定义的 5 参构造函数创建技能实例
            Skill skill = clazz.getConstructor(String.class, String.class, int.class, Role.class)
                    .newInstance(anno.skillName(), anno.skillId(),anno.requiredMana(), role);
            skill.execute(role, room);    // 执行技能逻辑

            System.out.println("释放成功，剩余技力：" + role.getCurrentMana());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}