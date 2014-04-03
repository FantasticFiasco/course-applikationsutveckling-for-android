package com.kindborg.mattias.animatedrobbertranslator;

import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.app.*;
import android.content.*;

public class MainActivity extends Activity {
	
	private View translateToButton;
	private View translateFromButton;
	private View aboutButton;
	private View quitButton;

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

        translateToButton.startAnimation(getFadeFromRightAnimation(0));
        translateFromButton.startAnimation(getFadeFromRightAnimation(200));
        aboutButton.startAnimation(getFadeFromRightAnimation(400));
        quitButton.startAnimation(getFadeFromRightAnimation(600));
    }

	private Animation getFadeFromRightAnimation(long offset) {
    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_from_right);
    	animation.setStartOffset(offset);
    	
    	return animation;
    }
}