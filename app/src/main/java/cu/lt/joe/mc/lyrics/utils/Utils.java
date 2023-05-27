package cu.lt.joe.mc.lyrics.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Utils
{
    public static int dpToPx(@NonNull Context context, int dp)
    {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createFile(@NonNull String path) throws java.io.IOException
    {
        int last_separator = path.lastIndexOf(File.separator);
        if (last_separator > 0)
            new File((path.substring(0, last_separator))).mkdirs();
        File file = new File(path);
        file.createNewFile();
    }

    @NonNull
    public static Drawable getBlurredDrawableFromView(@NonNull View view)
    {
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.draw(c);
        int width = Math.round(b.getWidth() * 0.5f), height = Math.round(b.getHeight() * 0.5f);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(b, width, height, false),
                outputBitmap = Bitmap.createBitmap(inputBitmap);
        Context context = view.getContext();
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap),
                tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(20f);//7.5 -> radius
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return new BitmapDrawable(context.getResources(), outputBitmap);
    }

    public static Bitmap getBlurredBitmap(@NonNull Context context, @NonNull Bitmap bitmap, float scaleFactor, float radius)
    {
        int width = Math.round(bitmap.getWidth() * scaleFactor), height = Math.round(bitmap.getHeight() * scaleFactor);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false),
                outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap),
                tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    @Nullable
    public static String getGoogleSearchLink(@NonNull String... keys)
    {
        try
        {
            StringBuilder queryBuilder = new StringBuilder();
            for (String s : keys)
                queryBuilder.append(s).append(" ");
            return "https://www.google.com/search?q=" + URLEncoder.encode(queryBuilder.toString().trim(), "utf8");
        }
        catch (UnsupportedEncodingException ignored)
        {
            return null;
        }
    }
}