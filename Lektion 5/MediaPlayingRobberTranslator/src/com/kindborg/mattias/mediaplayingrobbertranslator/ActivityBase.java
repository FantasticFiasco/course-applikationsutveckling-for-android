package com.kindborg.mattias.mediaplayingrobbertranslator;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;

/**
 * Base class for all activities.
 */
public abstract class ActivityBase extends Activity {

    /**
     * Retrieve a {@link SharedPreferences} object for specified class through
     * which you can retrieve and modify its values.
     * 
     * @param context
     *            The {@link Context} to get preferences from.
     * @return Returns the single {@link SharedPreferences} instance that can be
     *         used to retrieve and modify the preference values.
     */
    protected SharedPreferences getPreferences(Context context) {
        String name = context.getPackageName() + "_preferences";
        return getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * Create a new {@link SharedPreferences.Editor} for specified class,
     * through which you can make modifications to the data in the preferences
     * and atomically commit those changes back to the SharedPreferences object.
     * Note that you must call {@link SharedPreferences.Editor.commit()} to have
     * any changes you perform in the Editor actually show up in the
     * SharedPreferences.
     * 
     * @param context
     *            The {@link Context} to get preferences from.
     * @return Returns a new instance of the {@link SharedPreferences.Editor}
     *         interface, allowing you to modify the values in this
     *         SharedPreferences object.
     */
    protected Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }
}