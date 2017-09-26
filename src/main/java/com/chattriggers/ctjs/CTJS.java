package com.chattriggers.ctjs;

import com.chattriggers.ctjs.commands.CTCommand;
import com.chattriggers.ctjs.listeners.ChatListener;
import com.chattriggers.ctjs.listeners.WorldListener;
import com.chattriggers.ctjs.loader.ScriptLoader;
import com.chattriggers.ctjs.objects.DisplayHandler;
import com.chattriggers.ctjs.triggers.TriggerRegister;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.MODVERSION, clientSideOnly = true)
public class CTJS {
    @Getter
    private static CTJS instance;

    @Getter @Setter
    private ScriptEngine scriptEngine;
    @Getter @Setter
    private Invocable invocableEngine;
    @Getter @Setter
    private ScriptLoader scriptLoader;
    @Getter @Setter
    private DisplayHandler displayHandler;

    @EventHandler
    public void init(FMLInitializationEvent e) {
        instance = this;

        initMain(true);
        ClientCommandHandler.instance.registerCommand(new CTCommand());
    }

    public void initMain(boolean firstTime) {
        this.scriptEngine = new ScriptEngineManager(null).getEngineByName("nashorn");
        this.invocableEngine = ((Invocable) scriptEngine);

        if (!firstTime) {
            MinecraftForge.EVENT_BUS.unregister(scriptLoader);
            this.displayHandler.clearDisplays();
            TriggerRegister.TriggerTypes.clearAllTriggers();
        }

        if (firstTime) {
            this.displayHandler = new DisplayHandler();
        }

        this.scriptLoader = new ScriptLoader();
        MinecraftForge.EVENT_BUS.register(scriptLoader);

        if (firstTime) {
            MinecraftForge.EVENT_BUS.register(displayHandler);
            MinecraftForge.EVENT_BUS.register(new WorldListener());
            MinecraftForge.EVENT_BUS.register(new ChatListener());
        }
    }
}

class Reference {
    public static final String MODID = "ct.js";
    public static final String MODNAME = "ChatTriggers.js";
    public static final String MODVERSION = "0.1-SNAPSHOT";
}