package com.kindborg.mattias.dialpadapplication;

import android.app.*;
import android.content.*;
import android.os.*;

/**
 * Base class for all activities.
 */
public class BaseActivity extends Activity {

    /**
     * Gets value indicating whether specified {@link Bundle} has specified key.
     */
    protected boolean hasInstanceState(Bundle bundle, String key) {
        if (bundle == null) {
            return false;
        }

        return bundle.containsKey(key);
    }

    /**
     * Gets {@link String} with specified key from {@link Intent} extra.
     */
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