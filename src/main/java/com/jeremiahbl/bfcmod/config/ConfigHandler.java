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
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTimestamp;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFtbEssentials;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableLuckPerms;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMarkdown;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableColorsCommand;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTabListIntegration;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMetadataInTabList;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNicknamesInTabList;
		
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
			builder.pop();
		}
	}
}
