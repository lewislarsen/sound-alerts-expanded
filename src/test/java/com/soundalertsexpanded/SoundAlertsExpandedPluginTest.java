package com.soundalertsexpanded;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SoundAlertsExpandedPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SoundAlertsExpandedPlugin.class);
		RuneLite.main(args);
	}
}