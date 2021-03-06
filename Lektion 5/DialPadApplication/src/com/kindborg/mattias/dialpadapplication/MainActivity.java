package com.kindborg.mattias.dialpadapplication;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements DialPadView.IOnDialNumberListener {

    private static final String INSTANCESTATE_KEYSOUNDTYPE = "KEYSOUNDTYPE";
    private static final String INSTANCESTATE_NUMBER = "NUMBER";

    private DialPadView dialPadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialPadView = (DialPadView) findViewById(R.id.mainactivity_dialpadview);
        dialPadView.setOnDialNumberListener(this);

        // Restore instance state
        if (hasInstanceState(savedInstanceState, INSTANCESTATE_KEYSOUNDTYPE)) {
            dialPadView.setKeySoundType((DialPadView.KeySoundType) savedInstanceState.getSerializable(INSTANCESTATE_KEYSOUNDTYPE));
        }
        if (hasInstanceState(savedInstanceState, INSTANCESTATE_NUMBER)) {
            dialPadView.setNumber(savedInstanceState.getString(INSTANCESTATE_NUMBER));
        }

        // Inform user that no SD card exists
        if (!isExternalStorageReadable()) {
            Toast
                .makeText(this, R.string.mainactivity_nosdcard, Toast.LENGTH_SHORT)
                .show();
        }
    }

    @Override
    public void onDialNumber(String telephoneNumber) {
        // Create intent opening the phone dialer
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(telephoneNumber)));

        if (IntentExtensions.isIntentAvailable(this, intent)) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Make sure menu items are in sync with DialPadView settings
        MenuItem selectedKeySoundTypeMenuItem = dialPadView.getKeySoundType() == DialPadView.KeySoundType.beep ?
            menu.findItem(R.id.mainmenu_keysoundtype_beep) :
            menu.findItem(R.id.mainmenu_keysoundtype_voice);

        selectedKeySoundTypeMenuItem.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainmenu_keysoundtype_beep:
                dialPadView.setKeySoundType(DialPadView.KeySoundType.beep);
                item.setChecked(true);
                return true;

            case R.id.mainmenu_keysoundtype_voice:
                dialPadView.setKeySoundType(DialPadView.KeySoundType.sound);
                item.setChecked(true);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(INSTANCESTATE_KEYSOUNDTYPE, dialPadView.getKeySoundType());
        outState.putString(INSTANCESTATE_NUMBER, dialPadView.getNumber());
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) ||
            state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
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