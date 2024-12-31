/*
 * HerbPatches.java
 * Inspired by m0bilebtw/c-engineer-completed
 */

package com.soundalertsexpanded.triggers;

import com.soundalertsexpanded.SoundAlertsExpandedConfig;
import com.soundalertsexpanded.player.LoggedInState;
import com.soundalertsexpanded.sound.Sound;
import com.soundalertsexpanded.sound.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.GameTick;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class MissingCapeAtHerbPatches {

    private static final int MISSING_FARMING_CAPE_COOLDOWN = 20;

    // Define all herb patches in the game excluding the farming guild.
    private static final Set<Integer> FARMING_HERB_PATCH_CHUNKS = Set.of(
            782749, 782750, 922041, 403843, 403842, 682406, 682405,
            444859, 444860, 719281, 719280, 727531, 729579, 723405, 969058
    );

    // Define herb patches chunks surrounding the farming guild herb patch.
    private static final Set<Integer> FARMING_GUILD_HERB_PATCH_CHUNKS = Set.of(
            315858, 317905, 315857
    );

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

    private int lastCapeWarningTick = -1;

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        Player player = client.getLocalPlayer();
        WorldPoint wp = player.getWorldLocation();

        int tileX = wp.getX();
        int tileY = wp.getY();
        int chunkX = tileX >> 3;
        int chunkY = tileY >> 3;
        int chunkID = (chunkX << 11) | chunkY;

        if (shouldNotifyCape(chunkID)) {
            notifyAboutMissingCape();
        }
    }

    private boolean shouldNotifyCape(int chunkID) {
        return isInFarmingHerbPatch(chunkID) ||
                (isInFarmingGuildHerbPatch(chunkID) && config.announceMissingFarmCapeAtFarmingGuildHerbPatch());
    }

    private boolean isInFarmingHerbPatch(int chunkID) {
        return FARMING_HERB_PATCH_CHUNKS.contains(chunkID);
    }

    private boolean isInFarmingGuildHerbPatch(int chunkID) {
        return FARMING_GUILD_HERB_PATCH_CHUNKS.contains(chunkID);
    }

    private void notifyAboutMissingCape() {
        if (!config.announceMissingFarmCape() || loggedInState.isLoggedOut()) {
            return;
        }

        if (isCooldownActive() || client.getRealSkillLevel(Skill.FARMING) < 99) {
            return;
        }

        if (isCapeMissing()) {
            lastCapeWarningTick = client.getTickCount();
            soundEngine.playClip(Sound.FARMING_CAPE_NOT_EQUIPPED, executor);
        }
    }

    private boolean isCooldownActive() {
        return lastCapeWarningTick != -1 &&
                client.getTickCount() - lastCapeWarningTick < MISSING_FARMING_CAPE_COOLDOWN;
    }

    private boolean isCapeMissing() {
        ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);

        return equipment != null &&
                !equipment.contains(ItemID.FARMING_CAPE) &&
                !equipment.contains(ItemID.FARMING_CAPET) &&
                !equipment.contains(ItemID.MAX_CAPE);
    }
}
