package com.kindborg.mattias.robbertranslator;

import android.os.*;
import android.view.*;
import android.app.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
