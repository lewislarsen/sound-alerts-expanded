package com.soundalertsexpanded.triggers;

import com.soundalertsexpanded.SoundAlertsExpandedConfig;
import com.soundalertsexpanded.player.LoggedInState;
import com.soundalertsexpanded.sound.Sound;
import com.soundalertsexpanded.sound.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class ChambersOfXericCompletion {

    // Define the chat message indicating the completion of a Chambers of Xeric raid
    private static final String RAID_COMPLETION_MESSAGE = "Congratulations - your raid is complete!";

    @Inject
    private Client client;

    @Inject
    private SoundAlertsExpandedConfig config;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private LoggedInState loggedInState;

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() == ChatMessageType.GAMEMESSAGE &&
                chatMessage.getMessage().contains(RAID_COMPLETION_MESSAGE)) {
            notifyRaidCompletion();
        }
    }

    private void notifyRaidCompletion() {
        if (!config.announceChambersRaidCompletion() || loggedInState.isLoggedOut()) {
            return;
        }

        soundEngine.playClip(Sound.CHAMBERS_OF_XERIC_COMPLETION, executor);
    }
}