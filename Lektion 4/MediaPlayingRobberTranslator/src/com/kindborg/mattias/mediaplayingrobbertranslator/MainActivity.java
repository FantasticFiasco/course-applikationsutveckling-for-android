package com.kindborg.mattias.mediaplayingrobbertranslator;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;

public class MainActivity extends Activity {

    private static final String INSTANCESTATE_CURRENTPOSITION = "CURRENTPOSITION";

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
    protected void onStart() {
        super.onStart();
        
        // Start animations
        translateToButton.startAnimation(getFadeFromRightAnimation(0));
        translateFromButton.startAnimation(getFadeFromRightAnimation(200));
        aboutButton.startAnimation(getFadeFromRightAnimation(400));
        quitButton.startAnimation(getFadeFromRightAnimation(600));

        // Create media player
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer.seekTo(currentPosition);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
        currentPosition = mediaPlayer.getCurrentPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Release media player
        mediaPlayer.release();
        mediaPlayer = null;
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

    private Animation getFadeFromRightAnimation(long offset) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_from_right);
        animation.setStartOffset(offset);

        return animation;
    }
}