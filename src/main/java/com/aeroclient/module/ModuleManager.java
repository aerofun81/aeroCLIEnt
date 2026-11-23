package com.aeroclient.module;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();

    public static KillAura KILL_AURA;
    public static Freecam FREECAM;
    public static NoWeb NO_WEB;
    public static AutoTotem AUTO_TOTEM;
    public static Sprint SPRINT;
    public static Fullbright FULLBRIGHT;

    public static void init() {
        KILL_AURA = add(new KillAura());
        FREECAM = add(new Freecam());
        NO_WEB = add(new NoWeb());
        AUTO_TOTEM = add(new AutoTotem());
        SPRINT = add(new Sprint());
        FULLBRIGHT = add(new Fullbright());
    }

    private static <T extends Module> T add(T m) { MODULES.add(m); return m; }

    public static List<Module> all() { return MODULES; }

    public static void disableAll() {
        for (Module m : MODULES) m.setEnabled(false);
    }

    static final int UNBOUND = GLFW.GLFW_KEY_UNKNOWN;
}
