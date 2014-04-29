package com.kindborg.mattias.dialpadapplication;

import java.util.*;

import android.content.*;
import android.graphics.*;
import android.media.*;
import android.util.*;
import android.view.*;

public class DialPadView extends View implements View.OnLongClickListener {

    private static final int ROWS = 9;
    private static final int COLUMNS = 6;
    private static final int KEY_PADDING = 1;
    private static final int ROUNDED_CORNER = 5;

    private final List<Key> keys;
    private final Paint normalKeyForegroundPaint;
    private final Paint normalKeyBackgroundPaint;
    private final Paint pressedKeyBackgroundPaint;

    private IKeySound keySound;
    private IOnDialNumberListener onDialNumberListener;
    private StringBuilder numberBuilder;
    private RectF numberDestination;

    public DialPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Make sure view can take focus and thus receive keyboard events
        setFocusable(true);
        setFocusableInTouchMode(true);

        // Make sure view can receive long clicks
        setLongClickable(true);
        setOnLongClickListener(this);

        // Create all keys
        keys = new ArrayList<Key>();
        keys.add(new BackspaceKey(0, 4, 1, 1, R.drawable.dialpad_arrow, R.drawable.dialpad_arrow_pressed));
        keys.add(new PhoneKey(0, 5, 1, 1, R.drawable.dialpad_call, R.drawable.dialpad_call_pressed));
        keys.add(new Key('1', 1, 0, 2, 2, R.drawable.dialpad_1, R.drawable.dialpad_1_pressed, "one.mp3", ToneGenerator.TONE_DTMF_1));
        keys.add(new Key('2', 1, 2, 2, 2, R.drawable.dialpad_2, R.drawable.dialpad_2_pressed, "two.mp3", ToneGenerator.TONE_DTMF_2));
        keys.add(new Key('3', 1, 4, 2, 2, R.drawable.dialpad_3, R.drawable.dialpad_3_pressed, "three.mp3", ToneGenerator.TONE_DTMF_3));
        keys.add(new Key('4', 3, 0, 2, 2, R.drawable.dialpad_4, R.drawable.dialpad_4_pressed, "four.mp3", ToneGenerator.TONE_DTMF_4));
        keys.add(new Key('5', 3, 2, 2, 2, R.drawable.dialpad_5, R.drawable.dialpad_5_pressed, "five.mp3", ToneGenerator.TONE_DTMF_5));
        keys.add(new Key('6', 3, 4, 2, 2, R.drawable.dialpad_6, R.drawable.dialpad_6_pressed, "six.mp3", ToneGenerator.TONE_DTMF_6));
        keys.add(new Key('7', 5, 0, 2, 2, R.drawable.dialpad_7, R.drawable.dialpad_7_pressed, "seven.mp3", ToneGenerator.TONE_DTMF_7));
        keys.add(new Key('8', 5, 2, 2, 2, R.drawable.dialpad_8, R.drawable.dialpad_8_pressed, "eight.mp3", ToneGenerator.TONE_DTMF_8));
        keys.add(new Key('9', 5, 4, 2, 2, R.drawable.dialpad_9, R.drawable.dialpad_9_pressed, "nine.mp3", ToneGenerator.TONE_DTMF_9));
        keys.add(new Key('*', 7, 0, 2, 2, R.drawable.dialpad_star, R.drawable.dialpad_star_pressed, "star.mp3", ToneGenerator.TONE_DTMF_S));
        keys.add(new Key('0', 7, 2, 2, 2, R.drawable.dialpad_0, R.drawable.dialpad_0_pressed, "zero.mp3", ToneGenerator.TONE_DTMF_0));
        keys.add(new Key('#', 7, 4, 2, 2, R.drawable.dialpad_pound, R.drawable.dialpad_pound_pressed, "pound.mp3", ToneGenerator.TONE_DTMF_P));

        // Create rect where number should be drawn
        numberDestination = new RectF();

        // Create paints
        normalKeyForegroundPaint = createPaint(51, 181, 229);
        normalKeyBackgroundPaint = createPaint(50, 50, 50);
        pressedKeyBackgroundPaint = createPaint(150, 150, 150);

        // Create default key sound type
        setKeySoundType(KeySoundType.beep);

        // Create number builder
        numberBuilder = new StringBuilder();
    }

    /**
     * Gets the sound type played when a key is pressed.
     */
    public KeySoundType getKeySoundType() {
        return keySound.getType();
    }

    /**
     * Sets the sound type played when a key is pressed.
     */
    public void setKeySoundType(KeySoundType keySoundType) {
        // Release current key sound
        if (keySound != null) {
            keySound.release();
        }

        switch (keySoundType) {
            case beep:
                keySound = new BeepKeySound();
                break;

            case voice:
                keySound = new VoiceKeySound(getContext(), keys);
                break;

            default:
                throw new RuntimeException("Unsupported key sound type: " + keySoundType);
        }
    }

    /**
     * Gets the number displayed.
     */
    public String getNumber() {
        return numberBuilder.toString();
    }

    /**
     * Sets the number displayed.
     */
    public void setNumber(String number) {
        numberBuilder.setLength(0);
        numberBuilder.append(number);

        invalidate();
    }

    public void setOnDialNumberListener(IOnDialNumberListener onDialNumberListener) {
        this.onDialNumberListener = onDialNumberListener;
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
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Key pressedKey = null;

        for (Key key : keys) {
            if (key.key == event.getUnicodeChar()) {
                pressedKey = key;
                break;
            }
        }

        if (pressedKey != null) {
            trySetKeyState(pressedKey, true);
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

    @Override
    public boolean onLongClick(View v) {
        if (getPressedKey() instanceof BackspaceKey) {
            setNumber("");
            return true;
        }

        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float verticalOffset = h / ROWS;
        float horizontalOffset = w / COLUMNS;

        // The size of a key content needs to be square since all bitmaps are
        // square
        float cellContentSize = Math.min(verticalOffset, horizontalOffset);

        float left;
        float top;
        float right;
        float bottom;
        StringBuilder possibleKeys = new StringBuilder();

        // Update key destinations
        for (Key key : keys) {
            // Update key destination
            left = KEY_PADDING + key.column * horizontalOffset;
            top = KEY_PADDING + key.row * verticalOffset;
            right = (key.column + key.columnSpan) * horizontalOffset - KEY_PADDING;
            bottom = (key.row + key.rowSpan) * verticalOffset - KEY_PADDING;

            key.keyDestination.set(left, top, right, bottom);

            // Update key content destination
            left = key.keyDestination.centerX() - key.columnSpan * cellContentSize / 2;
            top = key.keyDestination.centerY() - key.rowSpan * cellContentSize / 2;
            right = left + key.columnSpan * cellContentSize;
            bottom = top + key.rowSpan * cellContentSize;

            key.keyContentDestination.set(
                left,
                top,
                right,
                bottom);

            possibleKeys.append(key.key);
        }

        // Update number size
        int numberKeyPadding = 6 * KEY_PADDING;
        left = numberKeyPadding;
        top = numberKeyPadding;
        right = 4 * horizontalOffset - numberKeyPadding;
        bottom = verticalOffset - numberKeyPadding;

        numberDestination.set(left, top, right, bottom);
        numberDestination = adjustTextSize(normalKeyForegroundPaint, numberDestination, possibleKeys.toString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw number
        String number = adjustText(numberBuilder.toString(), normalKeyForegroundPaint, numberDestination);
        canvas.drawText(number, numberDestination.left, numberDestination.top, normalKeyForegroundPaint);

        for (Key key : keys) {
            // Draw key
            canvas.drawRoundRect(
                key.keyDestination,
                ROUNDED_CORNER,
                ROUNDED_CORNER,
                key.isPressed ? pressedKeyBackgroundPaint : normalKeyBackgroundPaint);

            // Draw key content
            canvas.drawBitmap(
                key.isPressed ? key.pressedKey : key.normalKey,
                null,
                key.keyContentDestination,
                null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        keySound.release();
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

            if (isPressed) {
                key.onKeyPressed();
                keySound.play(key);
            } else {
                keySound.stop();
            }

            return true;
        }

        return false;
    }

    private static RectF adjustTextSize(Paint paint, RectF destination, String text) {
        // Set default text size
        float defaultTextSize = 100;
        paint.setTextSize(defaultTextSize);

        // Measure text bounds with default text size
        Rect defaultTextSizeBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), defaultTextSizeBounds);
        float scale = destination.height() / defaultTextSizeBounds.height();

        paint.setTextSize(scale * defaultTextSize);

        Rect newBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), newBounds);

        return new RectF(
            destination.left,
            destination.top - newBounds.top,
            destination.right,
            destination.bottom - newBounds.bottom);
    }

    private static String adjustText(String text, Paint paint, RectF destination) {
        if (paint.measureText(text) <= destination.width()) {
            return text;
        }

        // Number does not fit, lets trim it
        String adjustedText = "";
        String prefix = "...";

        for (int index = 1; index < text.length(); index++) {
            adjustedText = prefix + text.substring(index, text.length());

            if (paint.measureText(adjustedText) <= destination.width()) {
                break;
            }
        }

        return adjustedText;
    }

    private static Paint createPaint(int r, int g, int b) {
        Paint paint = new Paint();
        paint.setARGB(255, r, g, b);

        return paint;
    }

    /*
     * Enum describing the sound type played when a key is pressed.
     */
    public static enum KeySoundType {
        beep,
        voice
    }

    /**
     * Interface acting as listener for when a telephone number is dialed.
     */
    public interface IOnDialNumberListener {

        /**
         * Called when a telephone number is dialed.
         */
        void onDialNumber(String telephoneNumber);
    }

    /**
     * Class representing a key on the dial pad.
     */
    private class Key {

        public static final int SOUNDLESS_TONE = -1;
        public static final String SOUNDLESS_SOUND = "";

        private final char key;
        private final int row;
        private final int column;
        private final int rowSpan;
        private final int columnSpan;
        private final Bitmap normalKey;
        private final Bitmap pressedKey;
        private final String voiceSoundFileName;
        private final int beepToneType;
        private final RectF keyDestination;
        private final RectF keyContentDestination;

        private int voiceSoundId;
        private boolean isPressed;

        public Key(
            char key,
            int row,
            int column,
            int rowSpan,
            int columnSpan,
            int normalButtonResourceId,
            int pressedButtonResourceId,
            String voiceSoundFileName,
            int beepToneType) {
            this.key = key;
            this.row = row;
            this.column = column;
            this.rowSpan = rowSpan;
            this.columnSpan = columnSpan;
            this.normalKey = BitmapFactory.decodeResource(getResources(), normalButtonResourceId);
            this.pressedKey = BitmapFactory.decodeResource(getResources(), pressedButtonResourceId);
            this.voiceSoundFileName = voiceSoundFileName;
            this.beepToneType = beepToneType;
            keyDestination = new RectF();
            keyContentDestination = new RectF();
        }

        public void onKeyPressed() {
            numberBuilder.append(key);
        }
    }

    /*
     * Class representing a the phone key on the dial pad.
     */
    public class PhoneKey extends Key {

        public PhoneKey(
            int row,
            int column,
            int rowSpan,
            int columnSpan,
            int normalButtonResourceId,
            int pressedButtonResourceId) {
            super((char) -1, row, column, rowSpan, columnSpan, normalButtonResourceId, pressedButtonResourceId, Key.SOUNDLESS_SOUND, Key.SOUNDLESS_TONE);
        }

        @Override
        public void onKeyPressed() {
            if (onDialNumberListener != null) {
                onDialNumberListener.onDialNumber(numberBuilder.toString());
            }
        }
    }

    /*
     * Class representing a the backspace key on the dial pad.
     */
    public class BackspaceKey extends Key {

        public BackspaceKey(
            int row,
            int column,
            int rowSpan,
            int columnSpan,
            int normalButtonResourceId,
            int pressedButtonResourceId) {
            super((char) -1, row, column, rowSpan, columnSpan, normalButtonResourceId, pressedButtonResourceId, Key.SOUNDLESS_SOUND, Key.SOUNDLESS_TONE);
        }

        @Override
        public void onKeyPressed() {
            if (numberBuilder.length() > 0) {
                numberBuilder.deleteCharAt(numberBuilder.length() - 1);
            }
        }
    }

    /**
     * Interface capable of playing sounds for one of the key sound types.
     */
    private static interface IKeySound {

        /**
         * Gets the sound type this implementation is playing when a key is
         * pressed.
         */
        KeySoundType getType();

        /**
         * Plays a sound representing the specified key.
         */
        void play(Key key);

        /**
         * Stops the currently played sound.
         */
        void stop();

        /**
         * Release the resources of the implementation.
         */
        void release();
    }

    /**
     * Class responsible for playing voices as sound when a key is pressed.
     */
    private static class VoiceKeySound implements IKeySound {

        private SoundPool soundPool;

        public VoiceKeySound(Context context, List<Key> keys) {
            soundPool = new SoundPool(1, AudioManager.STREAM_DTMF, 0);

            // Load the sounds for all keys
            // for (Key key : keys) {
            // if (key.voiceSoundResourceId != Key.SOUNDLESS) {
            // key.voiceSoundId = soundPool.load(context,
            // key.voiceSoundResourceId, 1);
            // }
            // }
        }

        @Override
        public KeySoundType getType() {
            return KeySoundType.voice;
        }

        @Override
        public void play(Key key) {
            soundPool.play(key.voiceSoundId, 1, 1, 0, 0, 1);
        }

        @Override
        public void stop() {
            // It is not needed to stop the voice sound. Either a new sound is
            // played resulting in the previous to be stopped, or the sound
            // should be played to the end.
        }

        @Override
        public void release() {
            soundPool.release();
            soundPool = null;
        }
    }

    /**
     * Class responsible for playing beeps as sound when a key is pressed.
     */
    private static class BeepKeySound implements IKeySound {

        private ToneGenerator toneGenerator;

        public BeepKeySound() {
            toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, 100);
        }

        @Override
        public KeySoundType getType() {
            return KeySoundType.beep;
        }

        @Override
        public void play(Key key) {
            toneGenerator.startTone(key.beepToneType);
        }

        @Override
        public void stop() {
            toneGenerator.stopTone();
        }

        @Override
        public void release() {
            toneGenerator.release();
            toneGenerator = null;
        }
    }
}