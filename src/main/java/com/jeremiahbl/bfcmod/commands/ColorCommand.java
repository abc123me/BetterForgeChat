package com.jeremiahbl.bfcmod.commands;

import com.jeremiahbl.bfcmod.TextFormatter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ColorCommand {
	public static void register(CommandDispatcher<CommandSourceStack> disp) {
		disp.register(Commands.literal("colors").requires((c) -> {
			return c.hasPermission(1); }).executes(ctx -> colorCommand(ctx)));
		disp.register(Commands.literal("color").requires((c) -> {
			return c.hasPermission(1); }).executes(ctx -> colorCommand(ctx)));
	}
	
	public static int colorCommand(CommandContext<CommandSourceStack> ctx) {
		ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText(
				"Forge+LitePerms Chat Mod (c) Jeremiah Lowe 2022-2023\n"
						+ TextFormatter.colorString()), false);
		return 1;
	}
}
