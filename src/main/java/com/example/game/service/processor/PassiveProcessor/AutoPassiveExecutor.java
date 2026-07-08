package com.example.game.service.processor.PassiveProcessor;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.example.game.service.Room;
import com.example.game.service.tbs_entity.Role;

public class AutoPassiveExecutor {
    // 被动字段名 -> 被动实例
    private final Map<String, PassiveEffect> passiveMap = new HashMap<>();

    /**
     * 构造时自动扫描指定包下的所有 @Passive 类
     * @param packageName 要扫描的包名，如 "com.example.game.service.processor.PassiveProcessor.passives"
     */
    public AutoPassiveExecutor(String packageName, Room room) {
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
                            registerPassiveClass(className);
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

    private void registerPassiveClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Passive.class) &&
                    PassiveEffect.class.isAssignableFrom(clazz)) {
                Passive annotation = clazz.getAnnotation(Passive.class);
                String fieldName = annotation.value();
                PassiveEffect instance = (PassiveEffect) clazz.getDeclaredConstructor().newInstance();
                passiveMap.put(fieldName, instance);
                System.out.println("自动注册被动：" + fieldName + " -> " + className);
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
                registerPassiveClass(className);
            }
        }
    }

    /**
     * 根据被动字段名执行对应被动
     */
    public void execute(Role role, String passiveField, Room room) {
        PassiveEffect effect = passiveMap.get(passiveField);
        if (effect != null) {
            effect.apply(role, room);
        } else {
            System.out.println("未找到被动：" + passiveField);
            room.getStateProcessor().generateNewLine("未找到被动：" + passiveField);
        }
    }

    // 查看已注册的所有被动名
    public Set<String> getRegisteredPassives() {
        return passiveMap.keySet();
    }
}