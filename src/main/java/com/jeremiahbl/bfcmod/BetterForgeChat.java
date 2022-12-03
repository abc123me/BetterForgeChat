package com.jeremiahbl.bfcmod;

import com.jeremiahbl.bfcmod.events.ChatEventHandler;
import com.jeremiahbl.bfcmod.events.CommandRegistrationHandler;
import com.jeremiahbl.bfcmod.events.PermissionsModLoadingEvent;
import com.jeremiahbl.bfcmod.events.PlayerEventHandler;
import com.mojang.logging.LogUtils;

import net.luckperms.api.LuckPerms;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bfcmod")
public class BetterForgeChat {
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static BetterForgeChat instance;
	
	public LuckPerms perms = null;
	
    private ChatEventHandler chatHandler = new ChatEventHandler();
    private PermissionsModLoadingEvent permissionsLoadingEvent = new PermissionsModLoadingEvent();
    private CommandRegistrationHandler commandRegistrator = new CommandRegistrationHandler();
    private PlayerEventHandler playerEventHandler = new PlayerEventHandler();
    
    public BetterForgeChat() {
    	instance = this;
        // Register mod loading completed event
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.register(this);
        // Register commands
        MinecraftForge.EVENT_BUS.register(commandRegistrator);
    }
    private void loadComplete(final FMLLoadCompleteEvent e) {
    	// Register server chat event
    	MinecraftForge.EVENT_BUS.register(chatHandler);
    	// Register permissions mod API checking on server start
        MinecraftForge.EVENT_BUS.register(permissionsLoadingEvent);
    	// Register player events (NameFormat and TabListNameFormat)
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
    	LOGGER.info("Mod loaded OK! (c) Jeremiah Lowe 2022 - 2023!");
    }
}
