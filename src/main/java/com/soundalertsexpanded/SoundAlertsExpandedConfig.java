package com.soundalertsexpanded;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(SoundAlertsExpandedConfig.GROUP)
public interface SoundAlertsExpandedConfig extends Config {
	String GROUP = "soundalertsexpanded";

	@ConfigSection(
			name = "Sound Triggers",
			description = "Settings for various sound triggers.",
			position = 0
	)
	String SECTION_SOUND_TRIGGERS = "soundTriggers";

	@ConfigItem(
			keyName = "announceMissingFarmCape",
			name = "Notify Missing Farming/Max Cape",
			description = "Get an alert when at a herb patch without a Farming or Max Cape equipped.",
			section = SECTION_SOUND_TRIGGERS,
			position = 1
	)
	default boolean announceMissingFarmCape() {
		return true;
	}

	@ConfigItem(
			keyName = "announceMissingFarmCapeAtFarmingGuildHerbPatch",
			name = "Notify Missing Cape at Farming Guild",
			description = "Get an alert when at the Farming Guild herb patch without a Farming or Max Cape equipped.",
			section = SECTION_SOUND_TRIGGERS,
			position = 2
	)
	default boolean announceMissingFarmCapeAtFarmingGuildHerbPatch() {
		return true;
	}

	@Range(
			min = 0,
			max = 100
	)
	@ConfigItem(
			keyName = "soundVolume",
			name = "Sound Volume",
			description = "Adjust the volume of the sound alerts.",
			section = SECTION_SOUND_TRIGGERS,
			position = 3
	)
	default int soundVolume() {
		return 100;
	}
}