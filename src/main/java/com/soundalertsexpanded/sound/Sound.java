package com.soundalertsexpanded.sound;

import java.util.Random;

public enum Sound {
    FARMING_CAPE_NOT_EQUIPPED("FarmingCapeNotEquipped.wav"),
    CHAMBERS_OF_XERIC_COMPLETION("ChambersOfXericCompleted.wav"),
    ;

    private final String resourceName;

    Sound(String resNam) {
        this(resNam, false);
    }

    Sound(String resNam, boolean streamTroll) {
        resourceName = resNam;
    }

    public String getResourceName() {
        return resourceName;
    }

    private static final Random random = new Random();
}