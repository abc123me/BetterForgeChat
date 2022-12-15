package com.jeremiahbl.bfcmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ConfigBuilder config = new ConfigBuilder(builder);
	public static ForgeConfigSpec spec = builder.build();
	
	public static class ConfigBuilder {
		public final ForgeConfigSpec.ConfigValue<String> playerNameFormat;
		public final ForgeConfigSpec.ConfigValue<String> chatMessageFormat;
		public final ForgeConfigSpec.ConfigValue<String> timestampFormat;

		public final ForgeConfigSpec.ConfigValue<Integer> maximumNicknameLength;
		public final ForgeConfigSpec.ConfigValue<Integer> minimumNicknameLength;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTimestamp;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFtbEssentials;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableLuckPerms;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMarkdown;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableColorsCommand;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTabListIntegration;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMetadataInTabList;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNicknamesInTabList;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableWhoisCommand;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableChatNicknameCommand;
		public final ForgeConfigSpec.ConfigValue<Boolean> autoEnableChatNicknameCommand;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDiscordBotIntegration;

		public final ForgeConfigSpec.ConfigValue<String> discordBotToken;
		public final ForgeConfigSpec.ConfigValue<String> discordBotChannelID;
		public final ForgeConfigSpec.ConfigValue<String> discordServerStartMessage;
		public final ForgeConfigSpec.ConfigValue<String> discordServerStopMessage;
		public final ForgeConfigSpec.ConfigValue<String> discordPlayerJoinMessage;
		public final ForgeConfigSpec.ConfigValue<String> discordPlayerLeftMessage;
		public final ForgeConfigSpec.ConfigValue<String> discordPlayerChatMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDiscordServerMessages;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDiscordPlayerMessages;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDiscordDeathMessages;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDiscordNicknames;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDiscordMentions;
		
		public ConfigBuilder(ForgeConfigSpec.Builder builder) {
			builder.push("BetterForgeChatModConfig");
			playerNameFormat = builder
					.comment("  Controls the chat message format",
							 "    $prefix is replaced by the user's prefix or nothing if the user has no prefix",
							 "    $suffix is replaced by the user's suffix or nothing if the user has no suffix",
							 "    $name is replaced by the user's name, or nickname if they have one")
					.define("playerNameFormat", "$prefix$name$suffix");
			chatMessageFormat = builder
					.comment("  Controls the chat message format",
							 "    $time is replaced by the timestamp field or nothing if disabled", 
							 "    $name is replaced by the user's name, or nickname if they have one",
							 "    $msg is replaced by the username's message (if you use it more then once it WILL break this mod)")
					.define("chatMessageFormat", "$time | $name: $msg");
			timestampFormat = builder
					.comment("  Timestamp format following the java SimpleDateFormat",
							 "    Read more here: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html")
					.define("timestampFormat", "HH:mm");
			enableTimestamp = builder.comment("  Enables or disables the filling in of timestamps").define("enableTimestamp", true);
			enableFtbEssentials = builder.comment("  Enables or disables FTB essentials nickname integration").define("useFtbEssentials", true);
			enableLuckPerms = builder.comment("  Enables or disables LuckPerms integration").define("useLuckPerms", true);
			enableMarkdown = builder.comment("  Enables or disables markdown styling").define("markdownEnabled", true);
			enableTabListIntegration = builder.comment("  Enables or disables custom tab list information").define("tabList", true);
			enableMetadataInTabList = builder.comment("  Enables or disables prefixes&suffixes in the tab list").define("tabListMetadata", true);
			enableNicknamesInTabList = builder.comment("  Enables or disables nicknames in the tab list").define("tabListNicknames", true);
			enableColorsCommand = builder.comment("  Enables or disables the /colors command").define("enableColorsCommand", true);
			enableWhoisCommand = builder.comment(
					"  Enables or disables the integrated whois command"
				  + "    (If autoIntegratedNicknames is true, this setting is ignored) ").define("enableWhoisCommand", true);
			enableChatNicknameCommand = builder.comment(
					  "  Enables or disables the integrated nickname command"
					+ "   (If autoIntegratedNicknames is true, this setting is ignored) ").define("enableIntegratedNicknames", false);
			autoEnableChatNicknameCommand = builder.comment("  When true, enables the integrated nickname-related commands if FTB essentials is not present").define("autoIntegratedNicknames", true);
			maximumNicknameLength = builder.comment("  Maximum allowed nickname length (for integrated nickname commands)").defineInRange("maximumNicknameLength", 50, 1, 500);
			minimumNicknameLength = builder.comment("  Minimum allowed nickname length (for integrated nickname commands)").defineInRange("minimumNicknameLength", 1, 1, 500);
			enableDiscordBotIntegration = builder.comment("  Enables or disables discord integration").define("enableDiscordIntegration", false);
			builder.pop();
			
			builder.push("BetterForgeChatDiscordConfig");
			discordBotToken = builder.comment("  Discord bot token for discord integration").define("token", "");
			discordBotChannelID = builder.comment("  Numeric discord bot channel ID to send/receive player chat in").define("channelID", "0");
			enableDiscordDeathMessages = builder.comment("  When true, player death messages in discord (player tried to swim in lava)").define("deathMessages", true);
			enableDiscordPlayerMessages = builder.comment("  When true, player state messages in discord (player left/joined game)").define("playerMessages", true);
			enableDiscordServerMessages = builder.comment("  When true, server related messages appear in discord (server starting/stopping)").define("serverMessages", true);
			enableDiscordNicknames = builder.comment("  When true, in-game nicknames are shown in discord").define("nicknames", true);
			enableDiscordMentions = builder.comment("  Enables or disables the usage of discord mentions in in-game chat").define("mentions", false);
			discordServerStartMessage = builder.comment("  Message shown in discord when server starts").define("serverStartMsg", "***BetterForgeChatServer started!***");
			discordServerStopMessage = builder.comment("  Message shown in discord when server stops").define("serverStopMsg", "***BetterForgeChatServer stopped!***");
			discordPlayerLeftMessage = builder.comment("  Message shown when a player leaves, $user is username, $name is preferred name").define("playerLeftFormat", "***$name (aka. $user) left the game!***");
			discordPlayerJoinMessage = builder.comment("  Message shown when a player joins, $user is username, $name is preferred name").define("playerJoinFormat", "***$name (aka. $user) joined the game!***");
			discordPlayerChatMessage = builder.comment("  Message shown when a player uses in-game chat, $user is username, $name is preferred name, $msg is the chat message").define("chatFormat", "**[Ingame chat] $name (aka. $user)**: $msg");
			builder.pop();
		}
	}
}
