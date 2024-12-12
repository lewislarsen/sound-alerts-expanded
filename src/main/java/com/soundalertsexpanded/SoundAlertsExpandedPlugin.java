package com.soundalertsexpanded;

import com.google.inject.Provides;
import javax.inject.Inject;

import com.soundalertsexpanded.player.LoggedInState;
import com.soundalertsexpanded.sound.SoundEngine;
import com.soundalertsexpanded.sound.SoundFileManager;
import com.soundalertsexpanded.triggers.AnnouncementTriggers;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import okhttp3.OkHttpClient;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@PluginDescriptor(
	name = "Sound Alerts Expanded",
	description = "A RuneLite plugin that adds additional sound alert triggers throughout Gielinor.",
	tags = {"alerts", "sound", "announce"}
)
public class SoundAlertsExpandedPlugin extends Plugin
{
	@Inject
	private Client client;

	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	@Inject
	private SoundAlertsExpandedConfig config;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private EventBus eventBus;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private AnnouncementTriggers announcementTriggers;

	@Inject
	private LoggedInState loggedInState;

	@Override
	protected void startUp() throws Exception {
		eventBus.register(announcementTriggers);
		eventBus.register(loggedInState);
		loggedInState.setForCurrentGameState(client.getGameState());

		executor.submit(() -> SoundFileManager.prepareSoundFiles(okHttpClient, false));
	}

	@Override
	protected void shutDown() throws Exception {
		eventBus.unregister(announcementTriggers);
		eventBus.unregister(loggedInState);
		soundEngine.close();
	}

	@Provides
	SoundAlertsExpandedConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SoundAlertsExpandedConfig.class);
	}
}