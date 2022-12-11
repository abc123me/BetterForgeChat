package com.jeremiahbl.bfcmod;

import com.jeremiahbl.bfcmod.commands.NickCommands;
import com.jeremiahbl.bfcmod.config.*;
import com.jeremiahbl.bfcmod.events.*;
import com.jeremiahbl.bfcmod.utils.*;
import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterForgeChat.MODID)
public class BetterForgeChat {
	public static final String CHAT_ID_STR = 
			"&cBetter &9&lForge&r &eChat&r &d(c) Jeremiah Lowe 2022-2023&r\n";
	public static final String MODID = "bfcmod";
	public static final String VERSION = "V1.2.1";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static BetterForgeChat instance;
	
    public IMetadataProvider metadataProvider = null;
    public INicknameProvider nicknameProvider = null;
    
    private ChatEventHandler chatHandler = new ChatEventHandler();
    private ExternalModLoadingEvent modLoadingEvent = new ExternalModLoadingEvent();
    private PlayerEventHandler playerEventHandler = new PlayerEventHandler();
    private PermissionsHandler permissionsHandler = new PermissionsHandler();
    private CommandRegistrationHandler commandRegistrator = new CommandRegistrationHandler();
    private ConfigurationEventHandler configurationHandler = new ConfigurationEventHandler();
    
    public BetterForgeChat() {
    	instance = this;
    	// Register reloadable configuration stuff (reduce some things subscribed to the event bus)
    	configurationHandler.registerReloadable(playerEventHandler);
    	configurationHandler.registerReloadable(chatHandler);
    	configurationHandler.registerReloadable(() -> {
    		NickCommands.reloadConfig();
    		BetterForgeChatUtilities.reloadConfig();
    	});
    	configurationHandler.registerReloadable(() -> BetterForgeChat.LOGGER.info("Configuration options loaded!"));
    	MinecraftForge.EVENT_BUS.register(configurationHandler);
    	// Get the mod loading context (useful for doing stuff)
    	ModLoadingContext mlc = ModLoadingContext.get();
    	// Set mod to ignore whether or not the other side has it (we only care about the server side - but this works on the client's side too)
    	mlc.registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    	// Register mod configuration & permissions
    	mlc.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
    	MinecraftForge.EVENT_BUS.register(permissionsHandler);
    	// Register mod loading completed event
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        // Register the mod itself
        MinecraftForge.EVENT_BUS.register(this);
        // Register commands
        MinecraftForge.EVENT_BUS.register(commandRegistrator);
        
    }
    private void loadComplete(final FMLLoadCompleteEvent e) {
    	// Register server chat event
    	MinecraftForge.EVENT_BUS.register(chatHandler);
    	// Register permissions mod API checking on server start
        MinecraftForge.EVENT_BUS.register(modLoadingEvent);
    	// Register player events (NameFormat and TabListNameFormat)
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
        // Final mod loading completion message
    	LOGGER.info("Mod loaded up and ready to go! (c) Jeremiah Lowe 2022 - 2023!");
    }
}
