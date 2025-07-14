package com.soundalertsexpanded.sound;

import com.soundalertsexpanded.SoundAlertsExpandedConfig;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private SoundAlertsExpandedConfig config;

    @Inject
    private AudioPlayer audioPlayer;

    public void playClip(Sound sound, Executor executor) {
        executor.execute(() -> playClip(sound));
    }

    public void playClip(Sound sound, ScheduledExecutorService executor, Duration initialDelay) {
        executor.schedule(() -> playClip(sound), initialDelay.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void playClip(Sound sound) {
        float gain = 20f * (float) Math.log10(config.soundVolume() / 100f);

        try {
            audioPlayer.play(SoundFileManager.getSoundFile(sound), gain);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            log.warn("Failed to load Sound Alerts Expanded sound {}", sound, e);
        }
    }
}