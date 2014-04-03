package com.kindborg.mattias.dialpadapplication;

import java.util.*;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class DialPadView extends View {

    private static final int ROWS = 4;
    private static final int COLUMNS = 3;
    private static final int KEY_PADDING = 1;
    private static final int ROUNDED_CORNER = 5;

    private final List<Key> keys;
    private final Paint normalKeyPaint;
    private final Paint pressedKeyPaint;

    public DialPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Make sure view can take focus and thus receive keyboard events
        setFocusable(true);

        // Create all keys
        keys = new ArrayList<Key>();
        keys.add(createKey(0, 0, R.drawable.dialpad_1, R.drawable.dialpad_1_pressed));
        keys.add(createKey(0, 1, R.drawable.dialpad_2, R.drawable.dialpad_2_pressed));
        keys.add(createKey(0, 2, R.drawable.dialpad_3, R.drawable.dialpad_3_pressed));
        keys.add(createKey(1, 0, R.drawable.dialpad_4, R.drawable.dialpad_4_pressed));
        keys.add(createKey(1, 1, R.drawable.dialpad_5, R.drawable.dialpad_5_pressed));
        keys.add(createKey(1, 2, R.drawable.dialpad_6, R.drawable.dialpad_6_pressed));
        keys.add(createKey(2, 0, R.drawable.dialpad_7, R.drawable.dialpad_7_pressed));
        keys.add(createKey(2, 1, R.drawable.dialpad_8, R.drawable.dialpad_8_pressed));
        keys.add(createKey(2, 2, R.drawable.dialpad_9, R.drawable.dialpad_9_pressed));
        keys.add(createKey(3, 0, R.drawable.dialpad_star, R.drawable.dialpad_star_pressed));
        keys.add(createKey(3, 1, R.drawable.dialpad_0, R.drawable.dialpad_0_pressed));
        keys.add(createKey(3, 2, R.drawable.dialpad_pound, R.drawable.dialpad_pound_pressed));

        // Create key border paint
        normalKeyPaint = createPaint(50, 50, 50);
        pressedKeyPaint = createPaint(150, 150, 150);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float verticalOffset = h / ROWS;
        float horizontalOffset = w / COLUMNS;
        float keyHeight = verticalOffset - 2 * KEY_PADDING;
        float keyWidth = horizontalOffset - 2 * KEY_PADDING;

        // The size of a key content needs to be square since all bitmaps are
        // square
        float keyContentSize = Math.min(keyHeight, keyWidth);

        float x;
        float y;

        // Update key destinations
        for (Key key : keys) {
            // Update key destination
            x = KEY_PADDING + key.column * horizontalOffset;
            y = KEY_PADDING + key.row * verticalOffset;

            key.keyDestination.set(
                x,
                y,
                x + keyWidth,
                y + keyHeight);

            // Update key content destination
            x = key.keyDestination.centerX() - keyContentSize / 2;
            y = key.keyDestination.centerY() - keyContentSize / 2;

            key.keyContentDestination.set(
                x,
                y,
                x + keyContentSize,
                y + keyContentSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Key key : keys) {
            // Draw key
            canvas.drawRoundRect(
                key.keyDestination,
                ROUNDED_CORNER,
                ROUNDED_CORNER,
                key.isPressed ? pressedKeyPaint : normalKeyPaint);

            // Draw key content
            canvas.drawBitmap(
                key.isPressed ? key.pressedKey : key.normalKey,
                null,
                key.keyContentDestination,
                null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Key pressedKey;
        Key key;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Handle user pressing a key
                key = getKeyAt(event.getX(), event.getY());

                if (key != null) {
                    key.isPressed = true;
                    invalidate();
                }

                return true;

            case MotionEvent.ACTION_MOVE:
                // Handle user pressing one key but moving the finger to another
                // without releasing
                key = getKeyAt(event.getX(), event.getY());
                pressedKey = getPressedKey();

                if (key != pressedKey) {
                    if (pressedKey != null) {
                        pressedKey.isPressed = false;
                    }

                    if (key != null) {
                        key.isPressed = true;
                    }

                    invalidate();
                }

                return true;

            case MotionEvent.ACTION_UP:
                // Handle user releasing key
                pressedKey = getPressedKey();

                if (pressedKey != null) {
                    pressedKey.isPressed = false;
                    invalidate();
                }

                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isHandled = false;

        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                keys.get(0).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_2:
                keys.get(1).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_3:
                keys.get(2).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_4:
                keys.get(3).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_5:
                keys.get(4).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_6:
                keys.get(5).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_7:
                keys.get(6).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_8:
                keys.get(7).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_9:
                keys.get(8).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_STAR:
                keys.get(9).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_0:
                keys.get(10).isPressed = true;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_POUND:
                keys.get(11).isPressed = true;
                isHandled = true;
                break;
        }

        if (isHandled) {
            invalidate();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Key pressedKey = getPressedKey();

        if (pressedKey != null) {
            pressedKey.isPressed = false;
            invalidate();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    private Key getKeyAt(float x, float y) {
        for (Key key : keys) {
            if (key.keyDestination.contains(x, y)) {
                return key;
            }
        }

        return null;
    }

    private Key getPressedKey() {
        for (Key key : keys) {
            if (key.isPressed) {
                return key;
            }
        }

        return null;
    }

    private Key createKey(
        int row,
        int column,
        int normalButtonResourceId,
        int pressedButtonResourceId) {
        return new Key(
            row,
            column,
            BitmapFactory.decodeResource(getResources(), normalButtonResourceId),
            BitmapFactory.decodeResource(getResources(), pressedButtonResourceId));
    }

    private static Paint createPaint(int r, int g, int b) {
        Paint paint = new Paint();
        paint.setARGB(255, r, g, b);

        return paint;
    }

    /**
     * Class representing a key on the dial pad.
     */
    private static class Key {

        private final int row;
        private final int column;
        private final Bitmap normalKey;
        private final Bitmap pressedKey;
        private final RectF keyDestination;
        private final RectF keyContentDestination;

        private boolean isPressed;

        public Key(
            int row,
            int column,
            Bitmap normalKey,
            Bitmap pressedKey) {
            this.row = row;
            this.column = column;
            this.normalKey = normalKey;
            this.pressedKey = pressedKey;
            keyDestination = new RectF();
            keyContentDestination = new RectF();
        }
    }
}