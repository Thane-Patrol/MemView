/*
 * MemView - a simple photo viewer and converter written in Java
 *     Copyright (C) 2021 Hugh Mandalidis
 *     Contact: mandalidis.hugh@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>
 */

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
