package cu.lt.joe.mc.lyrics.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import cu.lt.joe.mc.lyrics.databinding.AdditionalInformationDialogLayoutBinding;

public class BaseActivity extends AppCompatActivity
{
    public final static int ALPHA_VALUE = 150;
    private int actionBarTintColor, foregroundColor, cardForegroundColor, cardBackgroundTintColor;
    private boolean navigateToHome;

    @BindingAdapter("android:backgroundColor")
    public static void setBackgroundColor(@NonNull View view, int color)
    {
        view.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @BindingAdapter("android:foregroundColor")
    public static void setForegroundColor(@NonNull TextView textView, int color)
    {
        textView.setTextColor(color);
    }

    @BindingAdapter("android:foregroundColor")
    public static void setForegroundColor(@NonNull ImageView imageView, int color)
    {
        imageView.setImageTintList(ColorStateList.valueOf(color));
    }

    @BindingAdapter("android:cardBackgroundColor")
    public static void setCardBackgroundTintColor(View view, int backgroundTintColor)
    {
        view.setBackgroundTintList(ColorStateList.valueOf(backgroundTintColor));
    }

    public int getActionBarTintColor()
    {
        return actionBarTintColor;
    }

    public void setActionBarTintColor(int color)
    {
        this.actionBarTintColor = color;
    }

    public int getForegroundColor()
    {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    public int getCardBackgroundTintColor()
    {
        return cardBackgroundTintColor;
    }

    public void setCardBackgroundTintColor(int cardBackgroundTintColor)
    {
        this.cardBackgroundTintColor = cardBackgroundTintColor;
    }

    public int getCardForegroundColor()
    {
        return cardForegroundColor;
    }

    public void setCardForegroundColor(int cardForegroundColor)
    {
        this.cardForegroundColor = cardForegroundColor;
    }

    public boolean getNavigateToHome()
    {
        return navigateToHome;
    }

    public void setNavigateToHome(boolean navigateToHome)
    {
        this.navigateToHome = navigateToHome;
    }

    public void onMenuExpanderClick(View menuExpanderView)
    {
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void openExternalLink(String link)
    {
        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)));
        }
        catch (Exception e)
        {
        }
    }

    public void showGeneralInfoDialog()
    {
        AdditionalInformationDialogLayoutBinding dialogLayoutBinding = AdditionalInformationDialogLayoutBinding.inflate(getLayoutInflater());
        dialogLayoutBinding.setActivity(this);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogLayoutBinding.getRoot())
                .create();
        dialogLayoutBinding.aidlOk.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
