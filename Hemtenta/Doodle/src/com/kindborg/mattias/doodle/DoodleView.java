package com.kindborg.mattias.doodle;

import static com.kindborg.mattias.doodle.Assert.*;

import java.util.*;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class DoodleView extends View {

    private final List<Point> points;
    private final Paint paint;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(Color.WHITE);

        points = new ArrayList<Point>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    /**
     * Gets the points that represents the doodle.
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Sets the points that represents the doodle.
     */
    public void setPoints(List<Point> points) {
        assertNotNull(points);

        this.points.clear();
        this.points.addAll(points);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        Point point = null;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                point = Point.create(x, y);
                break;

            case MotionEvent.ACTION_UP:
                point = Point.createStop(x, y);
                break;
        }

        if (point != null) {
            points.add(point);
            invalidate();
            return true;
        }
        else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Point previousPoint = null;

        for (Point point : points) {
            canvas.drawPoint(point.x, point.y, paint);

            if (previousPoint != null) {
                canvas.drawLine(previousPoint.x, previousPoint.y, point.x, point.y, paint);
            }

            previousPoint = point.isStop ? null : point;
        }
    }

    /**
     * Class representing a point drawn on the canvas.
     */
    public static class Point {

        private float x;
        private float y;
        private boolean isStop;

        public static Point create(float x, float y) {
            Point point = new Point();
            point.x = x;
            point.y = y;
            return point;
        }

        public static Point createStop(float x, float y) {
            Point point = create(x, y);
            point.isStop = true;
            return point;
        }

        /**
         * Gets the x-coordinate.
         */
        public float getX() {
            return x;
        }

        /**
         * Gets the y-coordinate.
         */
        public float getY() {
            return y;
        }

        /**
         * Gets a value indicating whether this point is a stop in a line.
         */
        public boolean getIsStop() {
            return isStop;
        }
    }
}