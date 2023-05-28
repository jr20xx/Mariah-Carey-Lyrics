package cu.lt.joe.mc.lyrics.activities;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.google.android.material.circularreveal.CircularRevealCompat;
import java.util.Objects;
import cu.lt.joe.mc.lyrics.R;
import cu.lt.joe.mc.lyrics.databinding.LyricsActivityLayoutBinding;
import cu.lt.joe.mc.lyrics.models.Album;
import cu.lt.joe.mc.lyrics.models.Song;
import cu.lt.joe.mc.lyrics.utils.Utils;

public class LyricsActivity extends BaseActivity
{
    private LyricsActivityLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.lyrics_activity_layout);
        Album album = getIntent().getParcelableExtra("album");
        Song song = getIntent().getParcelableExtra("song");
        setTitle(song.getTitle());
        int resourceId = album.getAlbumDrawableResourceId();
        Bitmap backgroundBitmap = ((BitmapDrawable) Objects.requireNonNull(ContextCompat.getDrawable(this, resourceId))).getBitmap();
        Glide.with(this).load(Utils.getBlurredBitmap(this, backgroundBitmap, 0.9f, 7)).placeholder(resourceId).centerCrop().into(binding.lyricsBackgroundIv);
        Palette palette = Palette.from(backgroundBitmap).generate();
        int dominantColorFullAlpha = palette.getDominantColor(Color.BLACK),
                dominantColorWithAlpha = ColorUtils.setAlphaComponent(dominantColorFullAlpha, 150),
                foregroundColor = ColorUtils.calculateLuminance(dominantColorFullAlpha) < 0.5 ? Color.WHITE : Color.BLACK;
        getWindow().setStatusBarColor(dominantColorFullAlpha);
        getWindow().setNavigationBarColor(dominantColorFullAlpha);
        setActionBarTintColor(dominantColorFullAlpha);
        setForegroundColor(foregroundColor);
        setCardBackgroundTintColor(dominantColorWithAlpha);
        setCardForegroundColor(foregroundColor);
        setNavigateToHome(true);
        binding.setActivity(this);
        binding.setSong(song);
        binding.setAlbum(album);
    }

    @Override
    public void onMenuExpanderClick(View menuExpanderView)
    {
        super.onMenuExpanderClick(menuExpanderView);
        binding.lyricsMenuContainer.setBackground(Utils.getBlurredDrawableFromView(binding.getRoot()));
        DisplayMetrics dim = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dim);
        int screenCenterX = (menuExpanderView.getLeft() + menuExpanderView.getRight()) / 2;
        int screenCenterY = (menuExpanderView.getTop() + menuExpanderView.getBottom()) / 2;
        float eStartRadius = 0;
        float eEndRadius = (float) Math.hypot(dim.widthPixels, dim.heightPixels);
        Animator expand = CircularRevealCompat.createCircularReveal(binding.lyricsMenuContainer, screenCenterX, screenCenterY, eStartRadius, eEndRadius);
        expand.setDuration(300);
        binding.lyricsMenuContainer.setVisibility(View.VISIBLE);
        expand.start();
    }

    @Override
    public void onBackPressed()
    {
        if (binding.lyricsMenuContainer.getVisibility() == View.VISIBLE)
        {
            DisplayMetrics dim = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dim);
            int screenCenterX = (binding.menuExpanderItem.getLeft() + binding.menuExpanderItem.getRight()) / 2;
            int screenCenterY = (binding.menuExpanderItem.getTop() + binding.menuExpanderItem.getBottom()) / 2;
            float eStartRadius = (float) Math.hypot(dim.widthPixels, dim.heightPixels);
            float eEndRadius = 0;
            Animator collapse = CircularRevealCompat.createCircularReveal(binding.lyricsMenuContainer, screenCenterX, screenCenterY, eStartRadius, eEndRadius);
            collapse.setDuration(300);
            collapse.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animator)
                {
                }

                @Override
                public void onAnimationEnd(Animator animator)
                {
                    binding.lyricsMenuContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator)
                {
                }

                @Override
                public void onAnimationRepeat(Animator animator)
                {
                }
            });
            collapse.start();
        }
        else
            super.onBackPressed();
    }
}