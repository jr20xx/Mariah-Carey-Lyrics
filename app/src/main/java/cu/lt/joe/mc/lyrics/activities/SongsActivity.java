package cu.lt.joe.mc.lyrics.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import java.util.ArrayList;
import cu.lt.joe.mc.lyrics.R;
import cu.lt.joe.mc.lyrics.adapters.SongsRecyclerAdapter;
import cu.lt.joe.mc.lyrics.databinding.SongsActivityMenuLayoutBinding;
import cu.lt.joe.mc.lyrics.models.Album;
import cu.lt.joe.mc.lyrics.models.Song;

public class SongsActivity extends ListActivity
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Album album = getIntent().getParcelableExtra("album");
        setTitle(album.getTitle());
        setBackground(album.getAlbumDrawableResourceId(), 0.9f, 5.8f);
        Palette palette = getPalette();
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
        ArrayList<Song> songs = album.getSongs();
        getList().setAdapter(new SongsRecyclerAdapter(this, songs, clickedSong ->
        {
            startActivity(new Intent(SongsActivity.this, LyricsActivity.class).putExtra("album", album).putExtra("song", clickedSong));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }));
        SongsActivityMenuLayoutBinding menuBinding = getMenuLayoutBinding();
        menuBinding.setAlbum(album);
        menuBinding.setActivity(this);
        menuBinding.songsCountTv.setText(getString(R.string.songs_count_end_label, songs.size()));
    }
}