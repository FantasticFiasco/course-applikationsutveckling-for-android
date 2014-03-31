package com.kindborg.mattias.calendarapplication;

import static com.kindborg.mattias.calendarapplication.assertion.Assert.*;

import android.support.v7.app.*;
import android.view.*;
import android.os.*;

public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Register on click listeners
		registerOnClickListener(findViewById(R.id.currentDateCalendarView));
		registerOnClickListener(findViewById(R.id.fixedDateCalendarView));
	}

	private static void registerOnClickListener(View view) {
		assertNotNull(view);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CalendarView calendarView = (CalendarView)v;
				calendarView.nextDay();
			}
		});
	}
}