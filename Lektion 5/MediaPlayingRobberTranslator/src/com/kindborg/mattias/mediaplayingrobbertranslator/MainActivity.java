package com.kindborg.mattias.mediaplayingrobbertranslator;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.media.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.animation.*;

public class MainActivity extends ActivityBase implements OnSharedPreferenceChangeListener {

    private static final String INSTANCESTATE_CURRENTPOSITION = "CURRENTPOSITION";
    private static final String PREFERENCE_PLAYMUSIC = "preference_playmusic";

    private View translateToButton;
    private View translateFromButton;
    private View aboutButton;
    private View quitButton;
    private MediaPlayer mediaPlayer;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        translateToButton = findViewById(R.id.translateToButton);
        translateFromButton = findViewById(R.id.translateFromButton);
        aboutButton = findViewById(R.id.aboutButton);
        quitButton = findViewById(R.id.quitButton);
    }

    public void onTranslateTo(View view) {
        Intent intent = new Intent(this, TranslateActivity.class);
        intent.putExtra(TranslateActivity.EXTRA_ISTRANSLATINGTOROBBER, true);
        startActivity(intent);
    }

    public void onTranslateFrom(View view) {
        Intent intent = new Intent(this, TranslateActivity.class);
        intent.putExtra(TranslateActivity.EXTRA_ISTRANSLATINGTOROBBER, false);
        startActivity(intent);
    }

    public void onAbout(View view) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.mainactivity_about)
            .setMessage(R.string.mainactivity_abouttext)
            .setPositiveButton(R.string.ok, null)
            .create()
            .show();
    }

    public void onQuit(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_main_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == PREFERENCE_PLAYMUSIC) {
            if (getIsPlayingMusic()) {
                executeMusicCommand(MusicCommand.play);
            } else {
                executeMusicCommand(MusicCommand.stop);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start animations
        translateToButton.startAnimation(getFadeFromRightAnimation(0));
        translateFromButton.startAnimation(getFadeFromRightAnimation(200));
        aboutButton.startAnimation(getFadeFromRightAnimation(400));
        quitButton.startAnimation(getFadeFromRightAnimation(600));

        // Start playing music if preferences say so
        if (getIsPlayingMusic()) {
            executeMusicCommand(MusicCommand.play);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start listening for preference changes
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        // Resume music
        executeMusicCommand(MusicCommand.resume);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop listening for preference changes
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

        // Pause music
        executeMusicCommand(MusicCommand.pause);
    }

    @Override
    protected void onStop() {
        super.onStop();

        executeMusicCommand(MusicCommand.stop);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save current position
        outState.putInt(INSTANCESTATE_CURRENTPOSITION, currentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentPosition = savedInstanceState.getInt(INSTANCESTATE_CURRENTPOSITION);
    }

    private void executeMusicCommand(MusicCommand command) {
        switch (command) {
            case play:
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.music);
                    mediaPlayer.setLooping(true);
                }
                break;

            case stop:
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                break;

            case pause:
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    currentPosition = mediaPlayer.getCurrentPosition();
                }
                break;

            case resume:
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(currentPosition);
                    mediaPlayer.start();
                }
                break;
        }
    }

    private Animation getFadeFromRightAnimation(long offset) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_from_right);
        animation.setStartOffset(offset);

        return animation;
    }

    private boolean getIsPlayingMusic() {
        return getPreferences(this).getBoolean(PREFERENCE_PLAYMUSIC, true);
    }

    /**
     * Enum describing different music commands.
     */
    private enum MusicCommand {
        play,
        stop,
        pause,
        resume
    }
}