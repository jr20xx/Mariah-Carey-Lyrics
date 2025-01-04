package cu.lt.joe.mc.lyrics.activities

import android.graphics.Color
import android.os.Bundle
import androidx.core.graphics.ColorUtils
import cu.lt.joe.mc.lyrics.R
import cu.lt.joe.mc.lyrics.adapters.AlbumRecyclerAdapter
import cu.lt.joe.mc.lyrics.databinding.AlbumsActivityMenuLayoutBinding
import cu.lt.joe.mc.lyrics.db.MainDatabaseHandler

class AlbumsActivity : ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.albums_activity_label)
        val backgroundResId = R.drawable.lovers
        setBackground(backgroundResId, 0.5f, 8f)
        val palette = palette
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
        val mdh = MainDatabaseHandler(this)
        list.adapter = AlbumRecyclerAdapter(this, mdh.albumsArray)
        val menuBinding = getMenuLayoutBinding<AlbumsActivityMenuLayoutBinding>()
        menuBinding.activity = this
    }
}