package com.kindborg.mattias.dialpadapplication;

import android.app.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {

    private static final String INSTANCESTATE_KEYSOUND = "KEYSOUND";

    private DialPadView dialPadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialPadView = (DialPadView) findViewById(R.id.mainactivity_dialpadview);

        // Restore key sound setting
        if (hasInstanceState(savedInstanceState, INSTANCESTATE_KEYSOUND)) {
            dialPadView.setKeySound((DialPadView.KeySound) savedInstanceState.getSerializable(INSTANCESTATE_KEYSOUND));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Make sure menu items are in sync with DialPadView settings
        MenuItem selectedKeySoundMenuItem = dialPadView.getKeySound() == DialPadView.KeySound.beep ?
            menu.findItem(R.id.mainmenu_keysound_beep) :
            menu.findItem(R.id.mainmenu_keysound_voice);

        selectedKeySoundMenuItem.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainmenu_keysound_beep:
                dialPadView.setKeySound(DialPadView.KeySound.beep);
                item.setChecked(true);
                return true;

            case R.id.mainmenu_keysound_voice:
                dialPadView.setKeySound(DialPadView.KeySound.voice);
                item.setChecked(true);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(INSTANCESTATE_KEYSOUND, dialPadView.getKeySound());

    }

    /**
     * Gets value indicating whether specified {@link Bundle} has specified key.
     */
    private boolean hasInstanceState(Bundle bundle, String key) {
        if (bundle == null) {
            return false;
        }

        return bundle.containsKey(key);
    }
}