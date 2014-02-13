package com.kindborg.mattias.robbertranslator;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.TextView;
import android.support.v4.app.*;
import android.annotation.*;

public class TranslateActivity extends Activity {

	public static final String EXTRA_ISTRANSLATINGTOROBBER = "com.kindborg.mattias.robbertranslator.TranslateActivity.EXTRA_ISTRANSLATINGTOROBBER";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_translate);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Determine whether the activity should translate to or from Robber Language
		boolean isTranslatingToRobber = getIsTranslatingToRobber();
		
		// Set title
		setTitle(isTranslatingToRobber ?
			R.string.translateto :
			R.string.translatefrom);
		
		// Set input description
		TextView inputDescription = (TextView)findViewById(R.id.translateactivity_inputdescription);
		inputDescription.setText(isTranslatingToRobber ?
			R.string.translateactivity_inputdescription_translatetorobber :
			R.string.translateactivity_inputdescription_translatefromrobber);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.translate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private boolean getIsTranslatingToRobber() {
		Bundle bundle = getIntent().getExtras();
		
		if (!bundle.containsKey(EXTRA_ISTRANSLATINGTOROBBER)) {
			throw new RuntimeException("Cannot start this activity without specifying EXTRA_ISTRANSLATINGTOROBBER");
		}
		
		return bundle.getBoolean(EXTRA_ISTRANSLATINGTOROBBER);
	}

}
