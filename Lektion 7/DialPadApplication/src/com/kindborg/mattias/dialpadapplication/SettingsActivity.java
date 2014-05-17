package com.kindborg.mattias.dialpadapplication;

import android.content.*;
import android.os.*;
import android.preference.*;

public class SettingsActivity extends PreferenceActivity {

    public static final String EXTRA_VOICEDIRECTORY = "extra_voicedirectory";
    public static final String KEY_SOUNDTYPE = "preference_soundtype";
    public static final String KEY_VOICEFILE = "preference_voicefile";

    private ListPreference voiceFile;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        voiceFile = (ListPreference) findPreference(KEY_VOICEFILE);
        populateVoiceFiles();
    }

    private void populateVoiceFiles() {
        String[] directoryPaths = ExternalStorage.getDirectories(getExtra(EXTRA_VOICEDIRECTORY));

        // Handle the fact that no voice files have been downloaded
        if (directoryPaths.length == 0) {
            voiceFile.setEnabled(false);
        }
        else {
            String[] directoryNames = ExternalStorage.getDirectoryNames(directoryPaths);

            voiceFile.setEntries(directoryNames);
            voiceFile.setEntryValues(directoryPaths);
        }
    }

    protected String getExtra(String key) {
        Intent intent = getIntent();

        if (intent == null) {
            return null;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return null;
        }

        if (!bundle.containsKey(key)) {
            throw new RuntimeException("Must specify " + key + " as extra when opening this activity.");
        }

        return bundle.getString(key);
    }
}