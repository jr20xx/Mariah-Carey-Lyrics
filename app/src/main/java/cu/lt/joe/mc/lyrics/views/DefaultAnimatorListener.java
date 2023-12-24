package cu.lt.joe.mc.lyrics.views;

import android.animation.Animator;
import androidx.annotation.NonNull;

/**
 * Originally written by <a href="https://github.com/hanks-zyh">Hanks</a> at <a href="https://github.com/hanks-zyh/HTextView">HTextView library repo</a>
 */
public class DefaultAnimatorListener implements Animator.AnimatorListener
{
    @Override
    public void onAnimationStart(@NonNull Animator animation)
    {
        // no-op
    }

    @Override
    public void onAnimationEnd(@NonNull Animator animation)
    {
        // no-op
    }

    @Override
    public void onAnimationCancel(@NonNull Animator animation)
    {
        // no-op
    }

    @Override
    public void onAnimationRepeat(@NonNull Animator animation)
    {
        // no-op
    }
}