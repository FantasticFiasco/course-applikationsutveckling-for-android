package com.kindborg.mattias.dialpadapplication;

import java.util.*;

import android.content.*;
import android.graphics.*;
import android.media.*;
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

    private IKeySound keySound;

    public DialPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Make sure view can take focus and thus receive keyboard events
        setFocusable(true);

        // Create all keys
        keys = new ArrayList<Key>();
        keys.add(createKey(0, 0, R.drawable.dialpad_1, R.drawable.dialpad_1_pressed, R.raw.one, ToneGenerator.TONE_DTMF_1));
        keys.add(createKey(0, 1, R.drawable.dialpad_2, R.drawable.dialpad_2_pressed, R.raw.two, ToneGenerator.TONE_DTMF_2));
        keys.add(createKey(0, 2, R.drawable.dialpad_3, R.drawable.dialpad_3_pressed, R.raw.three, ToneGenerator.TONE_DTMF_3));
        keys.add(createKey(1, 0, R.drawable.dialpad_4, R.drawable.dialpad_4_pressed, R.raw.four, ToneGenerator.TONE_DTMF_4));
        keys.add(createKey(1, 1, R.drawable.dialpad_5, R.drawable.dialpad_5_pressed, R.raw.five, ToneGenerator.TONE_DTMF_5));
        keys.add(createKey(1, 2, R.drawable.dialpad_6, R.drawable.dialpad_6_pressed, R.raw.six, ToneGenerator.TONE_DTMF_6));
        keys.add(createKey(2, 0, R.drawable.dialpad_7, R.drawable.dialpad_7_pressed, R.raw.seven, ToneGenerator.TONE_DTMF_7));
        keys.add(createKey(2, 1, R.drawable.dialpad_8, R.drawable.dialpad_8_pressed, R.raw.eight, ToneGenerator.TONE_DTMF_8));
        keys.add(createKey(2, 2, R.drawable.dialpad_9, R.drawable.dialpad_9_pressed, R.raw.nine, ToneGenerator.TONE_DTMF_9));
        keys.add(createKey(3, 0, R.drawable.dialpad_star, R.drawable.dialpad_star_pressed, R.raw.star, ToneGenerator.TONE_DTMF_S));
        keys.add(createKey(3, 1, R.drawable.dialpad_0, R.drawable.dialpad_0_pressed, R.raw.zero, ToneGenerator.TONE_DTMF_0));
        keys.add(createKey(3, 2, R.drawable.dialpad_pound, R.drawable.dialpad_pound_pressed, R.raw.pound, ToneGenerator.TONE_DTMF_P));

        // Create key border paint
        normalKeyPaint = createPaint(50, 50, 50);
        pressedKeyPaint = createPaint(150, 150, 150);

        // Create default key sound type
        setKeySoundType(KeySoundType.beep);
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
        int keyIndex = -1;

        switch (event.getUnicodeChar()) {
            case '1':
                keyIndex = 0;
                break;

            case '2':
                keyIndex = 1;
                break;

            case '3':
                keyIndex = 2;
                break;

            case '4':
                keyIndex = 3;
                break;

            case '5':
                keyIndex = 4;
                break;

            case '6':
                keyIndex = 5;
                break;

            case '7':
                keyIndex = 6;
                break;

            case '8':
                keyIndex = 7;
                break;

            case '9':
                keyIndex = 8;
                break;

            case '*':
                keyIndex = 9;
                break;

            case '0':
                keyIndex = 10;
                break;

            case '#':
                keyIndex = 11;
                break;
        }

        if (keyIndex != -1) {
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
                keySound.play(key);
            }
            else {
                keySound.stop();
            }

            return true;
        }

        return false;
    }

    private Key createKey(
        int row,
        int column,
        int normalButtonResourceId,
        int pressedButtonResourceId,
        int voiceSoundResourceId,
        int beepToneType) {
        return new Key(
            row,
            column,
            BitmapFactory.decodeResource(getResources(), normalButtonResourceId),
            BitmapFactory.decodeResource(getResources(), pressedButtonResourceId),
            voiceSoundResourceId,
            beepToneType);
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
     * Class representing a key on the dial pad.
     */
    private static class Key {

        private final int row;
        private final int column;
        private final Bitmap normalKey;
        private final Bitmap pressedKey;
        private final int voiceSoundResourceId;
        private final int beepToneType;
        private final RectF keyDestination;
        private final RectF keyContentDestination;

        private int voiceSoundId;
        private boolean isPressed;

        public Key(
            int row,
            int column,
            Bitmap normalKey,
            Bitmap pressedKey,
            int voiceSoundResourceId,
            int beepToneType) {
            this.row = row;
            this.column = column;
            this.normalKey = normalKey;
            this.pressedKey = pressedKey;
            this.voiceSoundResourceId = voiceSoundResourceId;
            this.beepToneType = beepToneType;
            keyDestination = new RectF();
            keyContentDestination = new RectF();
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
            for (Key key : keys) {
                key.voiceSoundId = soundPool.load(context, key.voiceSoundResourceId, 1);
            }
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