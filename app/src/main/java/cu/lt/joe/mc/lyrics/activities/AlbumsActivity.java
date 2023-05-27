package cu.lt.joe.mc.lyrics.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.graphics.ColorUtils;
import androidx.core.splashscreen.SplashScreen;
import androidx.palette.graphics.Palette;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import cu.lt.joe.mc.lyrics.R;
import cu.lt.joe.mc.lyrics.adapters.AlbumRecyclerAdapter;
import cu.lt.joe.mc.lyrics.databinding.AlbumsActivityMenuLayoutBinding;
import cu.lt.joe.mc.lyrics.db.MainDatabaseHandler;
import cu.lt.joe.mc.lyrics.utils.Utils;

public class AlbumsActivity extends ListActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.albums_activity_label);
        int backgroundResId = R.drawable.hall;
        setBackground(backgroundResId, 0.5f, 8);
        Palette palette = getPalette();
        int dominantColorFullAlpha = palette.getDominantColor(Color.BLACK),
                dominantColorWithAlpha = ColorUtils.setAlphaComponent(dominantColorFullAlpha, 148),
                foregroundColor = ColorUtils.calculateLuminance(dominantColorFullAlpha) < 0.5 ? Color.WHITE : Color.BLACK;
        getWindow().setStatusBarColor(dominantColorFullAlpha);
        getWindow().setNavigationBarColor(dominantColorFullAlpha);
        setActionBarTintColor(dominantColorFullAlpha);
        setForegroundColor(foregroundColor);
        setCardBackgroundTintColor(dominantColorWithAlpha);
        setCardForegroundColor(foregroundColor);
        transferSongsDatabase();
        MainDatabaseHandler mdh = new MainDatabaseHandler(this);
        getList().setAdapter(new AlbumRecyclerAdapter(this, mdh.getAlbumsArray()));
        AlbumsActivityMenuLayoutBinding menuBinding = getMenuLayoutBinding();
        menuBinding.setActivity(this);
    }

    private void transferSongsDatabase()
    {
        try
        {
            File destinyFile = new File(getApplicationInfo().dataDir + "/databases/songs.db");
            if (!destinyFile.exists())
            {
                InputStream is = getAssets().open("songs.db");
                Utils.createFile(destinyFile.getPath());
                FileOutputStream fos = new FileOutputStream(destinyFile);
                byte[] buffers = new byte[1024];
                int length;
                while ((length = is.read(buffers)) > 0)
                    fos.write(buffers, 0, length);
                is.close();
                fos.close();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}