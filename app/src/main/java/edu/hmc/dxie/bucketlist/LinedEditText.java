package edu.hmc.dxie.bucketlist;

/**
 * Created by DXie on 4/19/2015.
 *     Custom implementation of the EditText for the purpose of creating a "Notes" section for
 *     every bucket list item.  This LinedEditText class will act as an EditText but it will
 *     draw physical lines under every line of text to resemble a notepad.  Furthermore, it will
 *     limit the amount of space that the user can type.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class LinedEditText extends EditText {
    private Rect mRect;
    private Paint mPaint;
    private int maxLines = 10;

    // we need this constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLACK); //SET YOUR OWN COLOR HERE
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect r = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, r);//first line

        for (int i = 0; i < maxLines; i++) {
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            baseline += getLineHeight();//next line
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        TextWatcher watcher = new TextWatcher() {

            private String text;
            private int beforeCursorPosition = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //TODO sth
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                text = s.toString();
                beforeCursorPosition = start;
            }

            @Override
            public void afterTextChanged(Editable s) {

                // turning off listener
                removeTextChangedListener(this);

                // handling lines limit exceed
                if (LinedEditText.this.getLineCount() > maxLines) {
                    LinedEditText.this.setText(text);
                    LinedEditText.this.setSelection(beforeCursorPosition);
                }

                //turning on listener
                addTextChangedListener(this);

            }
        };

        this.addTextChangedListener(watcher);
    }
}