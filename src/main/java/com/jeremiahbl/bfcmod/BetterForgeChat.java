package com.jeremiahbl.bfcmod;

import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.events.ChatEventHandler;
import com.jeremiahbl.bfcmod.events.CommandRegistrationHandler;
import com.jeremiahbl.bfcmod.events.ExternalModLoadingEvent;
import com.jeremiahbl.bfcmod.events.PlayerEventHandler;
import com.jeremiahbl.bfcmod.utils.IMetadataProvider;
import com.jeremiahbl.bfcmod.utils.INicknameProvider;
import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterForgeChat.MODID)
public class BetterForgeChat {
	public static final String MODID = "bfcmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static BetterForgeChat instance;
	
    public IMetadataProvider metadataProvider = null;
    public INicknameProvider nicknameProvider = null;
    
    private ChatEventHandler chatHandler = new ChatEventHandler();
    private ExternalModLoadingEvent modLoadingEvent = new ExternalModLoadingEvent();
    private CommandRegistrationHandler commandRegistrator = new CommandRegistrationHandler();
    private PlayerEventHandler playerEventHandler = new PlayerEventHandler();
    private PermissionsHandler permissionsHandler = new PermissionsHandler();
    
    public BetterForgeChat() {
    	instance = this;
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
