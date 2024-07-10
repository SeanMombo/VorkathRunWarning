package com.blacksythe;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class VorkathRunWarningConfigTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(VorkathRunWarning.class);
		RuneLite.main(args);
	}
}