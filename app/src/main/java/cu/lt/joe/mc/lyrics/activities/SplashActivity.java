package cu.lt.joe.mc.lyrics.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import cu.lt.joe.mc.lyrics.R;
import cu.lt.joe.mc.lyrics.databinding.SplashScreenLayoutBinding;
import cu.lt.joe.mc.lyrics.utils.Utils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{
    private SplashScreenLayoutBinding binding;
    private Disposable databaseCheckerDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        binding = DataBindingUtil.setContentView(this, R.layout.splash_screen_layout);
        databaseCheckerDisposable = Observable.create((ObservableOnSubscribe<String>) emitter ->
                {
                    emitter.onNext(getString(R.string.checking_database_label));
                    File databaseFile = new File(getApplicationInfo().dataDir + "/databases/songs.db");
                    if (!databaseFile.exists())
                    {
                        emitter.onNext(getString(R.string.transferring_database_label));
                        transferDatabase();
                        emitter.onNext(getString(R.string.database_transferred_label));
                    }
                    else
                    {
                        emitter.onNext(getString(R.string.checking_db_integrity_label));
                        MessageDigest md = MessageDigest.getInstance("sha256");
                        FileInputStream fis = new FileInputStream(databaseFile);
                        byte buffer[] = new byte[1024];
                        int bytesCount;
                        while ((bytesCount = fis.read(buffer)) != -1)
                            md.update(buffer, 0, bytesCount);
                        fis.close();
                        final byte[] digest = md.digest();
                        final StringBuilder checksum = new StringBuilder();
                        for (byte b : digest)
                        {
                            String hex = Integer.toHexString(b & 0xff);
                            if (hex.length() == 1)
                                checksum.append("0");
                            checksum.append(hex);
                        }
                        if (!checksum.toString().equalsIgnoreCase("c643c87ceb773abb909c2d9c8f5bc7a8cbb5825b78333ae1c0e462a2a0a8271c"))
                        {
                            emitter.onNext(getString(R.string.replacing_database_label));
                            transferDatabase();
                            emitter.onNext(getString(R.string.database_replaced_label));
                        }
                    }
                    emitter.onNext(getString(R.string.made_in_cuba_label));
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> new Handler().postDelayed(() ->
                {
                    startActivity(new Intent(SplashActivity.this, AlbumsActivity.class));
                    SplashActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 3000))
                .subscribe(status -> binding.splashScreenTv.animateText(status),
                        throwable -> Toast.makeText(this, throwable.toString(), Toast.LENGTH_SHORT).show());
    }

    private void transferDatabase() throws IOException
    {
        File destinyFile = new File(getApplicationInfo().dataDir + "/databases/songs.db");
        InputStream is = getAssets().open("songs.db");
        Utils.createFile(destinyFile.getPath());
        FileOutputStream fos = new FileOutputStream(destinyFile);
        byte buffers[] = new byte[1024];
        int length;
        while ((length = is.read(buffers)) > 0)
            fos.write(buffers, 0, length);
        is.close();
        fos.close();
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (databaseCheckerDisposable != null && !databaseCheckerDisposable.isDisposed())
            databaseCheckerDisposable.dispose();
    }
}
