package com.kindborg.mattias.animatedrobbertranslator;

import android.os.*;
import android.view.*;
import android.app.*;
import android.content.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}