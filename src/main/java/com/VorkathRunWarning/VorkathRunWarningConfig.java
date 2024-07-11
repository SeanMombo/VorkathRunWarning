package com.VorkathRunWarning;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface VorkathRunWarningConfig extends Config
{
	@ConfigItem(
			position = 1,
			keyName = "showWorldType",
			name = "Show the current world type",
			description = "Toggle the display of the current world type"
	)

	default boolean showWorldType()
	{
		return false;
	}
	default String greeting()
	{
		return "Hello";
	}
}
