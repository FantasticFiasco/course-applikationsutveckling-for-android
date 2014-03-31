package com.kindborg.mattias.calendarapplication;

import static com.kindborg.mattias.calendarapplication.assertion.Assert.*;

import java.text.DateFormatSymbols;
import java.util.*;

import org.apache.http.impl.cookie.*;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.text.*;
import android.util.*;
import android.view.*;

public class CalendarView extends View {
	
	private final Bitmap background;
	private final RectF backgroundSize;
	private final TextPaint monthForeground;
	private final TextPaint dateForeground;
	private final TextPaint dayForeground;
		
	private Calendar calendar;
	
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_sheet);
		backgroundSize = new RectF();
		monthForeground = createTextPaint(Color.WHITE);
		dateForeground =  createTextPaint(Color.BLACK);
		dayForeground =  createTextPaint(Color.BLACK);
		
		calendar = Calendar.getInstance();
		
		// Make sure XML configuration is respected
		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarView, 0, 0);
		trySetDate(attributes.getString(R.styleable.CalendarView_date));
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
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// Update background size
		backgroundSize.right = w;
		backgroundSize.bottom = h;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Draw background
		canvas.drawBitmap(background, null, backgroundSize, null);
		
		// Draw month
		canvas.drawText(
			calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
			50,
			50,
			monthForeground);
		
		// Draw date
		canvas.drawText(
			Integer.toString(calendar.get(Calendar.DATE)),
			50,
			100,
			dateForeground);
		
		// Draw day
		canvas.drawText(
			calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
			50,
			150,
			dayForeground);
	}
	
	private static TextPaint createTextPaint(int color) {
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(color);
		textPaint.setAntiAlias(true);
		
		return textPaint;
	}
	
	private void trySetDate(String date) {
		if (date == null) {
			return;
		}
		
		try {
			calendar.setTime(DateUtils.parseDate(date));
		} catch (DateParseException e) {
			e.printStackTrace();
		}
	}
}