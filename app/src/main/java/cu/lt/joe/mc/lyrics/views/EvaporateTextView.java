package cu.lt.joe.mc.lyrics.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;
import cu.lt.joe.mc.lyrics.utils.CharacterUtils;

/**
 * Originally written by hanks.
 */
public class EvaporateTextView extends AppCompatTextView
{
    private final List<CharacterUtils.CharacterDiffResult> differentList = new ArrayList<>();
    private final List<Float> gapList = new ArrayList<>(), oldGapList = new ArrayList<>();
    private final int mostCount = 20;
    private final float charTime = 300;
    private int mTextHeight;
    private CharSequence mText, mOldText;
    private TextPaint mPaint, mOldPaint;
    private float progress, mTextSize, oldStartX;
    private AnimationListener animationListener;
    private long duration;
    private ValueAnimator animator;

    public EvaporateTextView(Context context)
    {
        this(context, null);
    }

    public EvaporateTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public EvaporateTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mOldText = "";
        mText = getText();
        progress = 1;

        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mOldPaint = new TextPaint(mPaint);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                EvaporateTextView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mTextSize = EvaporateTextView.this.getTextSize();
                oldStartX = 0;
                try
                {
                    int layoutDirection = ViewCompat.getLayoutDirection(EvaporateTextView.this);
                    oldStartX = layoutDirection == LAYOUT_DIRECTION_LTR ? EvaporateTextView.this.getLayout().getLineLeft(0) : EvaporateTextView.this.getLayout().getLineRight(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        prepareAnimate();

        animator = new ValueAnimator();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new DefaultAnimatorListener()
        {
            @Override
            public void onAnimationEnd(@NonNull Animator animation)
            {
                if (animationListener != null)
                    animationListener.onAnimationEnd(EvaporateTextView.this);
            }
        });
        animator.addUpdateListener(animation ->
        {
            progress = (float) animation.getAnimatedValue();
            invalidate();
        });
        int n = mText.length();
        n = n <= 0 ? 1 : n;
        duration = (long) (charTime + charTime / mostCount * (n - 1));

        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.END);
    }

    private void prepareAnimate()
    {
        mTextSize = getTextSize();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(getCurrentTextColor());
        mPaint.setTypeface(getTypeface());
        gapList.clear();
        for (int i = 0; i < mText.length(); i++)
            gapList.add(mPaint.measureText(String.valueOf(mText.charAt(i))));
        mOldPaint.setTextSize(mTextSize);
        mOldPaint.setColor(getCurrentTextColor());
        mOldPaint.setTypeface(getTypeface());
        oldGapList.clear();
        for (int i = 0; i < mOldText.length(); i++)
            oldGapList.add(mOldPaint.measureText(String.valueOf(mOldText.charAt(i))));
    }

    public void animateText(CharSequence text)
    {
        post(() ->
        {
            if (EvaporateTextView.this.getLayout() != null)
            {
                oldStartX = EvaporateTextView.this.getLayout().getLineLeft(0);
                EvaporateTextView.this.setText(text);
                mOldText = mText;
                mText = text;
                prepareAnimate();

                differentList.clear();
                differentList.addAll(CharacterUtils.diff(mOldText, mText));
                Rect bounds = new Rect();
                mPaint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
                mTextHeight = bounds.height();

                int n = mText.length();
                n = n <= 0 ? 1 : n;
                duration = (long) (charTime + charTime / mostCount * (n - 1));
                animator.cancel();
                animator.setFloatValues(0, 1);
                animator.setDuration(duration);
                animator.start();
            }
        });
    }

    public void setAnimationListener(AnimationListener listener)
    {
        animationListener = listener;
    }

    public float getProgress()
    {
        return this.progress;
    }

    /**
     * @param progress A floating point value between 0 and 1
     */
    public void setProgress(float progress)
    {
        this.progress = progress;
        invalidate();
    }

    public void onDraw(Canvas canvas)
    {
        float startX = getLayout().getLineLeft(0);
        float startY = getBaseline();

        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

        for (int i = 0; i < maxLength; i++)
        {
            // draw old text
            if (i < mOldText.length())
            {
                float pp = progress * duration / (charTime + charTime / mostCount * (mText.length() - 1));

                mOldPaint.setTextSize(mTextSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1)
                {
                    mOldPaint.setAlpha(255);
                    float p = pp * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gapList, oldGapList);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                }
                else
                {
                    mOldPaint.setAlpha((int) ((1 - pp) * 255));
                    float y = startY - pp * mTextHeight;
                    float width = mOldPaint.measureText(mOldText.charAt(i) + "");
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGapList.get(i) - width) / 2, y, mOldPaint);
                }
                oldOffset += oldGapList.get(i);
            }

            // draw new text
            if (i < mText.length())
            {
                if (!CharacterUtils.stayHere(i, differentList))
                {
                    int alpha = (int) (255f / charTime * (progress * duration - charTime * i / mostCount));
                    alpha = Math.min(alpha, 255);
                    alpha = Math.max(alpha, 0);

                    mPaint.setAlpha(alpha);
                    mPaint.setTextSize(mTextSize);
                    float pp = progress * duration / (charTime + charTime / mostCount * (mText.length() - 1));
                    float y = mTextHeight + startY - pp * mTextHeight;

                    float width = mPaint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gapList.get(i) - width) / 2, y, mPaint);
                }

                offset += gapList.get(i);
            }
        }
    }
}