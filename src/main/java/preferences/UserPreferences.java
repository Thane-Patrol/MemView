package preferences;

import java.util.prefs.Preferences;

public class UserPreferences {
    private Preferences userPreferences;

    public UserPreferences() {
        userPreferences = Preferences.userRoot();

    }

    //True for show GPS tag
    public void setMetadataGPSLabel(boolean state) {
        userPreferences.putBoolean("SHOW_GPS_METADATA", state);
    }
    //todo change all defaults to false when proper menu functionality is implemented
    public boolean getMetadataGPSLabel() {
        return userPreferences.getBoolean("SHOW_GPS_METADATA", false);
    }

    //True for show Creation date
    public void setMetadataCreationLabel(boolean state) {
        userPreferences.putBoolean("SHOW_CREATION_METADATA", state);
    }

    public boolean getMetadataCreationLabel() {
        return userPreferences.getBoolean("SHOW_CREATION_METADATA", false);
    }

    public void setMetadataFileSizeLabel(boolean state) {
        userPreferences.putBoolean("SHOW_FILE_SIZE_METADATA", state);
    }

    public boolean getMetadataFileSizeLabel() {
        return userPreferences.getBoolean("SHOW_FILE_SIZE_METADATA", false);
    }
}
