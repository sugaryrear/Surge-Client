package com.client.sound;

import com.client.features.settings.Preferences;

public enum SoundType {
    MUSIC, SOUND, AREA_SOUND
    ;

    public double getVolume() {
        switch (this) {
            case MUSIC:
                return 0;
            case SOUND:
                return Preferences.getPreferences().soundVolume;
            case AREA_SOUND:
                return 0;
            default:
                throw new IllegalStateException("Didn't handle " + toString());
        }
    }
}
