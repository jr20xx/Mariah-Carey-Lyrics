package cu.lt.joe.mc.lyrics.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Song implements Parcelable
{
    public static final Creator<Song> CREATOR = new Creator<>()
    {
        @Override
        public Song createFromParcel(Parcel in)
        {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size)
        {
            return new Song[size];
        }
    };
    private final Long SONG_ID;
    private final String title, lyrics, writers, yt_link;

    public Song(Long SONG_ID, String title, String lyrics, String writers, String yt_link)
    {
        this.SONG_ID = SONG_ID;
        this.title = title;
        this.lyrics = lyrics;
        this.writers = writers;
        this.yt_link = yt_link;
    }

    protected Song(Parcel in)
    {
        SONG_ID = in.readLong();
        title = in.readString();
        lyrics = in.readString();
        writers = in.readString();
        yt_link = in.readString();
    }

    public Long getSongID()
    {
        return SONG_ID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getLyrics()
    {
        return lyrics;
    }

    public String getWriters()
    {
        return writers;
    }

    public String getYtLink()
    {
        return yt_link;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeLong(SONG_ID);
        dest.writeString(title);
        dest.writeString(lyrics);
        dest.writeString(writers);
        dest.writeString(yt_link);
    }
}
