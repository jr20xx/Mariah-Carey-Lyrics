package cu.lt.joe.mc.lyrics.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.graphics.ColorUtils
import cu.lt.joe.mc.lyrics.R
import cu.lt.joe.mc.lyrics.adapters.SongsRecyclerAdapter
import cu.lt.joe.mc.lyrics.databinding.SongsActivityMenuLayoutBinding
import cu.lt.joe.mc.lyrics.models.Album
import cu.lt.joe.mc.lyrics.models.Song

class SongsActivity : ListActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val album = intent.getParcelableExtra<Album>("album")
        title = album!!.title
        setBackground(album.albumDrawableResourceId, 0.9f, 5.8f)
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
        navigateToHome = true
        val songs = album.songs
        list.adapter = SongsRecyclerAdapter(this, songs) { clickedSong: Song? ->
            startActivity(Intent(this@SongsActivity, LyricsActivity::class.java).putExtra("album", album)
                .putExtra("song", clickedSong))
        }
        val menuBinding = getMenuLayoutBinding<SongsActivityMenuLayoutBinding>()
        menuBinding.album = album
        menuBinding.activity = this
        menuBinding.songsCountTv.text = getString(R.string.songs_count_end_label, songs!!.size)
    }
}