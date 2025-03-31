package cu.lt.joe.mc.lyrics.activities;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.circularreveal.CircularRevealCompat;
import java.util.Objects;
import cu.lt.joe.mc.lyrics.R;
import cu.lt.joe.mc.lyrics.databinding.ListActivityLayoutBinding;
import cu.lt.joe.mc.lyrics.utils.Utils;

public class ListActivity extends BaseActivity
{
    private ListActivityLayoutBinding binding;
    private RecyclerView list;
    private Palette palette;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.list_activity_layout);
        list = binding.list;
        list.addItemDecoration(new RecyclerView.ItemDecoration()
        {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
            {
                if (parent.getChildAdapterPosition(view) == 0)
                    outRect.top = Utils.dpToPx(ListActivity.this, 66);
            }
        });
        binding.setActivity(this);
    }

    public RecyclerView getList()
    {
        return list;
    }

    public void setBackground(int resourceId, float scaleFactor, float blurRadius)
    {
        Bitmap backgroundBitmap = ((BitmapDrawable) Objects.requireNonNull(ContextCompat.getDrawable(this, resourceId))).getBitmap();
        Glide.with(this).load(Utils.getBlurredBitmap(this, backgroundBitmap, scaleFactor, blurRadius)).placeholder(resourceId).centerCrop().into(binding.listBackgroundIv);
        palette = Palette.from(backgroundBitmap).generate();
    }

    public Palette getPalette()
    {
        return palette;
    }

    @SuppressWarnings("unchecked")
    public <T extends androidx.databinding.ViewDataBinding> T getMenuLayoutBinding()
    {
        if (this instanceof AlbumsActivity)
            return (T) binding.albumsMenuLayout;
        else
            return (T) binding.songsMenuLayout;
    }

    @Override
    public void onMenuExpanderClick(View menuExpanderView)
    {
        super.onMenuExpanderClick(menuExpanderView);
        binding.listMenuContainer.setBackground(Utils.getBlurredDrawableFromView(binding.getRoot()));
        DisplayMetrics dim = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dim);
        int screenCenterX = (menuExpanderView.getLeft() + menuExpanderView.getRight()) / 2;
        int screenCenterY = (menuExpanderView.getTop() + menuExpanderView.getBottom()) / 2;
        float eStartRadius = 0;
        float eEndRadius = (float) Math.hypot(dim.widthPixels, dim.heightPixels);
        Animator expand = CircularRevealCompat.createCircularReveal(binding.listMenuContainer, screenCenterX, screenCenterY, eStartRadius, eEndRadius);
        expand.setDuration(300);
        binding.listMenuContainer.setVisibility(View.VISIBLE);
        expand.start();
    }

    @Override
    public void onBackPressed()
    {
        if (binding.listMenuContainer.getVisibility() == View.VISIBLE)
        {
            DisplayMetrics dim = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dim);
            int screenCenterX = (binding.menuExpanderItem.getLeft() + binding.menuExpanderItem.getRight()) / 2;
            int screenCenterY = (binding.menuExpanderItem.getTop() + binding.menuExpanderItem.getBottom()) / 2;
            float eStartRadius = (float) Math.hypot(dim.widthPixels, dim.heightPixels);
            float eEndRadius = 0;
            Animator collapse = CircularRevealCompat.createCircularReveal(binding.listMenuContainer, screenCenterX, screenCenterY, eStartRadius, eEndRadius);
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
                    binding.listMenuContainer.setVisibility(View.GONE);
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
