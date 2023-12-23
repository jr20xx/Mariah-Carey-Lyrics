package cu.lt.joe.mc.lyrics.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import cu.lt.joe.mc.lyrics.R;
import cu.lt.joe.mc.lyrics.adapters.AlbumRecyclerAdapter;
import cu.lt.joe.mc.lyrics.databinding.AlbumsActivityMenuLayoutBinding;
import cu.lt.joe.mc.lyrics.db.MainDatabaseHandler;

public class AlbumsActivity extends ListActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.albums_activity_label);
        int backgroundResId = R.drawable.lovers;
        setBackground(backgroundResId, 0.5f, 8);
        Palette palette = getPalette();
        int dominantColorFullAlpha = palette.getDominantColor(Color.BLACK),
                dominantColorWithAlpha = ColorUtils.setAlphaComponent(dominantColorFullAlpha, ALPHA_VALUE),
                foregroundColor = ColorUtils.calculateLuminance(dominantColorFullAlpha) < 0.5 ? Color.WHITE : Color.BLACK;
        getWindow().setStatusBarColor(dominantColorFullAlpha);
        getWindow().setNavigationBarColor(dominantColorFullAlpha);
        setActionBarTintColor(dominantColorFullAlpha);
        setForegroundColor(foregroundColor);
        setCardBackgroundTintColor(dominantColorWithAlpha);
        setCardForegroundColor(foregroundColor);
        MainDatabaseHandler mdh = new MainDatabaseHandler(this);
        getList().setAdapter(new AlbumRecyclerAdapter(this, mdh.getAlbumsArray()));
        AlbumsActivityMenuLayoutBinding menuBinding = getMenuLayoutBinding();
        menuBinding.setActivity(this);
    }
}