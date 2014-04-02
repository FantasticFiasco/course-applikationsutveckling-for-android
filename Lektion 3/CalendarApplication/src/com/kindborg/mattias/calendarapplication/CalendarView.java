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
	private final RectF backgroundSize;
	private final TextGraphics monthGraphics;
	private final TextGraphics dateGraphics;
	private final TextGraphics dayGraphics;
		
	private Calendar calendar;
	
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_sheet);
		backgroundSize = new RectF();
		monthGraphics = new TextGraphics(Color.WHITE, false);
		dateGraphics = new TextGraphics(Color.BLACK, true);
		dayGraphics = new TextGraphics(Color.BLACK, false);
				
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
		monthGraphics.destination.left = left;
		monthGraphics.destination.right = right;
		monthGraphics.destination.top = 0.25f * h;
		monthGraphics.destination.bottom = 0.3825f * h;
		
		// Calculate date destination
		dateGraphics.destination.left = left;
		dateGraphics.destination.right = right;
		dateGraphics.destination.top = 0.48f * h;
		dateGraphics.destination.bottom = 0.78f * h;
		
		// Calculate day destination
		dayGraphics.destination.left = left;
		dayGraphics.destination.right = right;
		dayGraphics.destination.top = 0.83f * h;
		dayGraphics.destination.bottom = 0.95f * h;
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
			monthGraphics);
		
		// Draw date
		drawText(
			Integer.toString(calendar.get(Calendar.DATE)),
			canvas,
			dateGraphics);
		
		// Draw day
		drawText(
			calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
			canvas,
			dayGraphics);
	}
	
	private static void drawText(String text, Canvas canvas, TextGraphics textGraphics) {
		RectF adjustedDestination = adjustTextSize(textGraphics, text);
		
		canvas.drawText(
			text,
			adjustedDestination.centerX(),
			adjustedDestination.centerY(),
			textGraphics.paint);
	}
	
	private static RectF adjustTextSize(TextGraphics textGraphics, String text) {
		// Set default text size
		float defaultTextSize = 100;
		textGraphics.paint.setTextSize(defaultTextSize);
		
		// Measure text bounds with default text size
	    Rect defaultTextSizeBounds = new Rect();
	    textGraphics.paint.getTextBounds(text, 0, text.length(), defaultTextSizeBounds);
	    
	    // Compensate text size to match desired destination but make sure to respect both width and
	    // height of the destination
	    float scale = Math.min(
	    	textGraphics.destination.height() / defaultTextSizeBounds.height(),
	    	textGraphics.destination.width() / defaultTextSizeBounds.width());
	    
	    textGraphics.paint.setTextSize(scale * defaultTextSize);
	    
	    // Measure new text bounds based on compensated text size
	    Rect newBounds = new Rect();
	    textGraphics.paint.getTextBounds(text, 0, text.length(), newBounds);
	    
	    return new RectF(
	    	textGraphics.destination.left,
	    	textGraphics.destination.top - newBounds.top,
	    	textGraphics.destination.right,
	    	textGraphics.destination.bottom - newBounds.bottom);
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
	
	private static class TextGraphics {
		
		private final TextPaint paint;
		private final RectF destination;
		
		public TextGraphics(int color, Boolean isShadowed) {
			destination = new RectF();
			paint = new TextPaint();
			paint.setColor(color);
			paint.setAntiAlias(true);
			paint.setTextAlign(Align.CENTER);
			
			if (isShadowed) {
				paint.setShadowLayer(1, 2, 2, color);	
			}
		}
	}
}