package com.jeremiahbl.bfcmod.events;

import com.jeremiahbl.bfcmod.commands.ColorCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommandRegistrationHandler {
	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent e) {
		ColorCommand.register(e.getDispatcher());
	}
}
