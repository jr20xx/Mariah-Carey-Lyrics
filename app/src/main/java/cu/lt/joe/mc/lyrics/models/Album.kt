package cu.lt.joe.mc.lyrics.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import java.util.ArrayList;
import cu.lt.joe.mc.lyrics.R;

public class Album implements Parcelable
{
    public static final Creator<Album> CREATOR = new Creator<>()
    {
        @Override
        public Album createFromParcel(Parcel in)
        {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size)
        {
            return new Album[size];
        }
    };
    private final Long ALBUM_ID;
    private final String title, recording_label, wikiLink;
    private final ArrayList<Song> songs;
    private final int year;

    public Album(Long ALBUM_ID, String title, String recording_label, ArrayList<Song> songs, int year, String wikiLink)
    {
        this.ALBUM_ID = ALBUM_ID;
        this.title = title;
        this.recording_label = recording_label;
        this.songs = songs;
        this.year = year;
        this.wikiLink = wikiLink;
    }

    protected Album(Parcel in)
    {
        ALBUM_ID = in.readLong();
        title = in.readString();
        recording_label = in.readString();
        songs = in.createTypedArrayList(Song.CREATOR);
        year = in.readInt();
        wikiLink = in.readString();
    }

    @BindingAdapter("android:src")
    public static void setAlbumDrawable(ImageView iv, int resourceId)
    {
        Glide.with(iv).load(resourceId).into(iv);
    }

    @BindingAdapter("android:roundSrc")
    public static void setAlbumRoundDrawable(ImageView iv, int resourceId)
    {
        Glide.with(iv).load(resourceId).transform(new CircleCrop()).into(iv);
    }

    public int getAlbumDrawableResourceId()
    {
        switch (ALBUM_ID.intValue())
        {
            case 1:
                return R.drawable.mc;
            case 2:
                return R.drawable.emotions;
            case 3:
                return R.drawable.music_box;
            case 4:
                return R.drawable.merry_christmas;
            case 5:
                return R.drawable.daydream;
            case 6:
                return R.drawable.butterfly;
            case 7:
                return R.drawable.rainbow;
            case 8:
                return R.drawable.glitter;
            case 9:
                return R.drawable.charmbracelet;
            case 10:
                return R.drawable.emancipation;
            case 11:
                return R.drawable.emcc;
            case 12:
                return R.drawable.memoirs;
            case 13:
                return R.drawable.merry_christmas_2;
            case 14:
                return R.drawable.me_i_am_mariah;
            case 15:
                return R.drawable.caution;
            default:
                return R.drawable.splash;
        }
    }

    public Long getALBUM_ID()
    {
        return ALBUM_ID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getRecording_label()
    {
        return recording_label;
    }

    public ArrayList<Song> getSongs()
    {
        return songs;
    }

    public String getYear()
    {
        return year + "";
    }

    public String getWikiLink()
    {
        return wikiLink;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeLong(ALBUM_ID);
        dest.writeString(title);
        dest.writeString(recording_label);
        dest.writeTypedList(songs);
        dest.writeInt(year);
        dest.writeString(wikiLink);
    }
}
