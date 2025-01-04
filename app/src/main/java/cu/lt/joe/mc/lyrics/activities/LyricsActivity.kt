package cu.lt.joe.mc.lyrics.activities

import android.animation.Animator
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.google.android.material.circularreveal.CircularRevealCompat
import cu.lt.joe.mc.lyrics.R
import cu.lt.joe.mc.lyrics.databinding.LyricsActivityLayoutBinding
import cu.lt.joe.mc.lyrics.models.Album
import cu.lt.joe.mc.lyrics.models.Song
import cu.lt.joe.mc.lyrics.utils.Utils
import java.util.Objects

class LyricsActivity : BaseActivity() {
    private lateinit var binding: LyricsActivityLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.lyrics_activity_layout)
        val album = intent.getParcelableExtra<Album>("album")
        val song = intent.getParcelableExtra<Song>("song")
        title = song!!.title
        val resourceId = album!!.albumDrawableResourceId
        val backgroundBitmap = (Objects.requireNonNull(
            ContextCompat.getDrawable(
                this,
                resourceId
            )
        ) as BitmapDrawable).bitmap
        Glide.with(this).load(Utils.getBlurredBitmap(this, backgroundBitmap, 0.9f, 7f))
            .placeholder(resourceId).centerCrop().into(binding.lyricsBackgroundIv)
        val palette = Palette.from(backgroundBitmap).generate()
        val dominantColorFullAlpha = palette.getDominantColor(Color.BLACK)
        val dominantColorWithAlpha =
            ColorUtils.setAlphaComponent(dominantColorFullAlpha, ALPHA_VALUE)
        foregroundColor =
            if (ColorUtils.calculateLuminance(dominantColorFullAlpha) < 0.5) Color.WHITE else Color.BLACK
        window.statusBarColor = dominantColorFullAlpha
        window.navigationBarColor = dominantColorFullAlpha
        actionBarTintColor = dominantColorFullAlpha
        cardBackgroundTintColor = dominantColorWithAlpha
        cardForegroundColor = foregroundColor
        navigateToHome = true
        binding.activity = this
        binding.song = song
        binding.album = album
    }

    override fun onMenuExpanderClick(menuExpanderView: View) {
        super.onMenuExpanderClick(menuExpanderView)
        binding.lyricsMenuContainer.background = Utils.getBlurredDrawableFromView(binding.root)
        val dim = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dim)
        val screenCenterX = (menuExpanderView.left + menuExpanderView.right) / 2
        val screenCenterY = (menuExpanderView.top + menuExpanderView.bottom) / 2
        val eStartRadius = 0f
        val eEndRadius =
            Math.hypot(dim.widthPixels.toDouble(), dim.heightPixels.toDouble()).toFloat()
        val expand = CircularRevealCompat.createCircularReveal(
            binding.lyricsMenuContainer,
            screenCenterX.toFloat(),
            screenCenterY.toFloat(),
            eStartRadius,
            eEndRadius
        )
        expand.duration = 300
        binding.lyricsMenuContainer.visibility = View.VISIBLE
        expand.start()
    }

    override fun onBackPressed() {
        if (binding.lyricsMenuContainer.visibility == View.VISIBLE) {
            val dim = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dim)
            val screenCenterX =
                (binding.menuExpanderItem.left + binding.menuExpanderItem.right) / 2
            val screenCenterY =
                (binding.menuExpanderItem.top + binding.menuExpanderItem.bottom) / 2
            val eStartRadius =
                Math.hypot(dim.widthPixels.toDouble(), dim.heightPixels.toDouble()).toFloat()
            val eEndRadius = 0f
            val collapse = CircularRevealCompat.createCircularReveal(
                binding.lyricsMenuContainer,
                screenCenterX.toFloat(),
                screenCenterY.toFloat(),
                eStartRadius,
                eEndRadius
            )
            collapse.duration = 300
            collapse.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    binding.lyricsMenuContainer.visibility = View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            collapse.start()
        } else super.onBackPressed()
    }
}