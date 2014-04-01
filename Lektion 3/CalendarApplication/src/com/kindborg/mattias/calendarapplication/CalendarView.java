package com.kindborg.mattias.calendarapplication;

import static com.kindborg.mattias.calendarapplication.assertion.Assert.*;

import java.util.*;

import org.apache.http.impl.cookie.*;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Paint.*;
import android.text.*;
import android.util.*;
import android.view.*;

public class CalendarView extends View {
	
	private final Bitmap background;
	private final TextPaint monthForeground;
	private final TextPaint dateForeground;
	private final TextPaint dayForeground;
	private final RectF backgroundSize;
	private final RectF monthForegroundDestination;
	private final RectF dateForegroundDestination;
	private final RectF dayForegroundDestination;
		
	private Calendar calendar;
	
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_sheet);
		monthForeground = createTextPaint(Color.WHITE, false);
		dateForeground = createTextPaint(Color.BLACK, true);
		dayForeground = createTextPaint(Color.BLACK, false);
		
		backgroundSize = new RectF();
		monthForegroundDestination = new RectF();
		dateForegroundDestination = new RectF();
		dayForegroundDestination = new RectF();
		
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
		
		// Force layout pass
		invalidate();
	}
	
	/**
	 * Updates the view to display the next day.
	 */
	public void nextDay() {
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		
		// Force layout pass
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// Update background size
		backgroundSize.right = w;
		backgroundSize.bottom = h;
		
		// Define left and right padding
		float left = 0.05f * w;
		float right = 0.95f * w;
		
		// Calculate month destination
		monthForegroundDestination.left = left;
		monthForegroundDestination.right = right;
		monthForegroundDestination.top = 0.25f * h;
		monthForegroundDestination.bottom = 0.3825f * h;
		
		// Calculate date destination
		dateForegroundDestination.left = left;
		dateForegroundDestination.right = right;
		dateForegroundDestination.top = 0.48f * h;
		dateForegroundDestination.bottom = 0.78f * h;
		
		// Calculate day destination
		dayForegroundDestination.left = left;
		dayForegroundDestination.right = right;
		dayForegroundDestination.top = 0.83f * h;
		dayForegroundDestination.bottom = 0.95f * h;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Draw background
		canvas.drawBitmap(background, null, backgroundSize, null);
		
		// Draw month
		drawText(
			calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
			canvas,
			monthForeground,
			monthForegroundDestination);
		
		// Draw date
		drawText(
			Integer.toString(calendar.get(Calendar.DATE)),
			canvas,
			dateForeground,
			dateForegroundDestination);
		
		// Draw day
		drawText(
			calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
			canvas,
			dayForeground,
			dayForegroundDestination);
	}
	
	private static void drawText(String text, Canvas canvas, Paint paint, RectF destination) {
		RectF adjustedDestination = adjustTextSize(paint, destination, text);
		
		canvas.drawText(
			text,
			adjustedDestination.centerX(),
			adjustedDestination.centerY(),
			paint);
	}
	
	private static RectF adjustTextSize(Paint paint, RectF destination, String text) {
		// Set default text size
		float defaultTextSize = 100;
		paint.setTextSize(defaultTextSize);
		
		// Measure text bounds with default text size
	    Rect defaultTextSizeBounds = new Rect();
	    paint.getTextBounds(text, 0, text.length(), defaultTextSizeBounds);
	    
	    // Compensate text size to match desired destination but make sure to respect both width and
	    // height of the destination
	    float scale = Math.min(
    		destination.height() / defaultTextSizeBounds.height(),
    		destination.width() / defaultTextSizeBounds.width());
	    
	    paint.setTextSize(scale * defaultTextSize);
	    
	    // Measure new text bounds based on compensated text size
	    Rect newBounds = new Rect();
	    paint.getTextBounds(text, 0, text.length(), newBounds);
	    
	    return new RectF(
	    	destination.left,
	    	destination.top - newBounds.top,
	    	destination.right,
	    	destination.bottom - newBounds.bottom);
	}
	
	private static TextPaint createTextPaint(int color, Boolean isShadowed) {
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(color);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
		
		if (isShadowed) {
			textPaint.setShadowLayer(1, 2, 2, color);	
		}
		
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