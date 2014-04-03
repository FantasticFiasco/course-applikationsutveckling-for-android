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
        boolean isHandled = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Handle user pressing a key
                if (trySetKeyState(getKeyAt(event.getX(), event.getY()), true)) {
                    isHandled = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Handle user pressing one key but moving the finger to another
                // without releasing
                Key key = getKeyAt(event.getX(), event.getY());
                Key pressedKey = getPressedKey();

                if (key != pressedKey) {
                    trySetKeyState(pressedKey, false);
                    trySetKeyState(key, true);
                    isHandled = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                // Handle user releasing key
                if (trySetKeyState(getPressedKey(), false)) {
                    isHandled = true;
                }
                break;
        }

        if (isHandled) {
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isHandled = false;
        int keyIndex = -1;

        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                keyIndex = 0;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_2:
                keyIndex = 1;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_3:
                keyIndex = 2;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_4:
                keyIndex = 3;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_5:
                keyIndex = 4;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_6:
                keyIndex = 5;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_7:
                keyIndex = 6;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_8:
                keyIndex = 7;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_9:
                keyIndex = 8;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_STAR:
                keyIndex = 9;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_0:
                keyIndex = 10;
                isHandled = true;
                break;

            case KeyEvent.KEYCODE_POUND:
                keyIndex = 11;
                isHandled = true;
                break;
        }

        if (isHandled) {
            trySetKeyState(keys.get(keyIndex), true);
            invalidate();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (trySetKeyState(getPressedKey(), false)) {
            invalidate();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * Gets key located at specified x- and y-coordinate.
     * 
     * @return Key at specified x- and y-coordinate if one exist; otherwise
     *         null.
     */
    private Key getKeyAt(float x, float y) {
        for (Key key : keys) {
            if (key.keyDestination.contains(x, y)) {
                return key;
            }
        }

        return null;
    }

    /**
     * Gets first key that is in a pressed state.
     * 
     * @return The first key that is in a pressed state if one exist; otherwise
     *         null.
     */
    private Key getPressedKey() {
        for (Key key : keys) {
            if (key.isPressed) {
                return key;
            }
        }

        return null;
    }

    /**
     * Tries to set specified pressed state to specified key.
     * 
     * @return true if it was possible to set pressed state on key, i.e. key
     *         wasn't null; otherwise false.
     */
    private boolean trySetKeyState(Key key, boolean isPressed) {
        if (key != null) {
            key.isPressed = isPressed;
            return true;
        }

        return false;
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