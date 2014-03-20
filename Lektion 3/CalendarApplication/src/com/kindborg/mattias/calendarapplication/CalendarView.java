package com.kindborg.mattias.calendarapplication;

import static com.kindborg.mattias.calendarapplication.assertion.Assert.*;

import java.util.*;

import org.apache.http.impl.cookie.*;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class CalendarView extends View {
	
	private Calendar calendar;
	
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Default value
		calendar = Calendar.getInstance();
		
		// Make sure XML configuration is respected
		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarView, 0, 0);
		String date = attributes.getString(R.styleable.CalendarView_date);
		if (date != null) {
			try {
				calendar.setTime(DateUtils.parseDate(date));
			} catch (DateParseException e) {
				e.printStackTrace();
			}
		}
		
		attributes.recycle();
	}
	
	/**
	 * Gets the date displayed in the view.
	 * @return The date displayed in the view.
	 */
	public Calendar getDate() {
		return calendar;
	}
	
	/**
	 * Updates view to display specified date.
	 * @param date The date to display in the view.
	 */
	public void setDate(Calendar date) {
		assertNotNull(date);
		
		calendar = date;
	}
	
	/**
	 * Updates the view to display the next day.
	 */
	public void nextDay() {
		calendar.add(Calendar.DAY_OF_MONTH, 1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// See http://catchthecows.com/?p=72
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}