package cu.lt.joe.mc.lyrics.activities

import android.animation.Animator
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bumptech.glide.Glide
import com.google.android.material.circularreveal.CircularRevealCompat
import cu.lt.joe.mc.lyrics.R
import cu.lt.joe.mc.lyrics.databinding.ListActivityLayoutBinding
import cu.lt.joe.mc.lyrics.utils.Utils
import java.util.Objects
import kotlin.math.hypot

open class ListActivity : BaseActivity() {
    private lateinit var binding: ListActivityLayoutBinding
    protected lateinit var list: RecyclerView
    protected lateinit var palette: Palette

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.list_activity_layout)
        list = binding.list
        list.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (parent.getChildAdapterPosition(view) == 0) outRect.top =
                    Utils.dpToPx(this@ListActivity, 66)
            }
        })
        binding.activity = this
    }

    fun setBackground(resourceId: Int, scaleFactor: Float, blurRadius: Float) {
        val backgroundBitmap =
            (Objects.requireNonNull(ContextCompat.getDrawable(this, resourceId)) as BitmapDrawable).bitmap
        Glide.with(this)
            .load(Utils.getBlurredBitmap(this, backgroundBitmap, scaleFactor, blurRadius))
            .placeholder(resourceId).centerCrop().into(binding.listBackgroundIv)
        palette = Palette.from(backgroundBitmap).generate()
    }

    fun <T : ViewDataBinding?> getMenuLayoutBinding(): T {
        return if (this is AlbumsActivity) binding.albumsMenuLayout as T else binding.songsMenuLayout as T
    }

    override fun onMenuExpanderClick(menuExpanderView: View) {
        super.onMenuExpanderClick(menuExpanderView)
        binding.listMenuContainer.background = Utils.getBlurredDrawableFromView(binding.root)
        val dim = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dim)
        val screenCenterX = (menuExpanderView.left + menuExpanderView.right) / 2
        val screenCenterY = (menuExpanderView.top + menuExpanderView.bottom) / 2
        val eStartRadius = 0f
        val eEndRadius =
            hypot(dim.widthPixels.toDouble(), dim.heightPixels.toDouble()).toFloat()
        val expand =
            CircularRevealCompat.createCircularReveal(binding.listMenuContainer, screenCenterX.toFloat(), screenCenterY.toFloat(), eStartRadius, eEndRadius)
        expand.duration = 300
        binding.listMenuContainer.visibility = View.VISIBLE
        expand.start()
    }

    override fun onBackPressed() {
        if (binding.listMenuContainer.visibility == View.VISIBLE) {
            val dim = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dim)
            val screenCenterX =
                (binding.menuExpanderItem.left + binding.menuExpanderItem.right) / 2
            val screenCenterY =
                (binding.menuExpanderItem.top + binding.menuExpanderItem.bottom) / 2
            val eStartRadius =
                hypot(dim.widthPixels.toDouble(), dim.heightPixels.toDouble()).toFloat()
            val eEndRadius = 0f
            val collapse =
                CircularRevealCompat.createCircularReveal(binding.listMenuContainer, screenCenterX.toFloat(), screenCenterY.toFloat(), eStartRadius, eEndRadius)
            collapse.duration = 300
            collapse.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    binding.listMenuContainer.visibility = View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            collapse.start()
        } else super.onBackPressed()
    }
}