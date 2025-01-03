package cu.lt.joe.mc.lyrics.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class Song : Parcelable {
    val SONG_ID: Long

    @JvmField
    val title: String?

    @JvmField
    val lyrics: String?

    @JvmField
    val writers: String?
    val ytLink: String?

    constructor(SONG_ID: Long, title: String?, lyrics: String?, writers: String?, yt_link: String?) {
        this.SONG_ID = SONG_ID
        this.title = title
        this.lyrics = lyrics
        this.writers = writers
        ytLink = yt_link
    }

    protected constructor(`in`: Parcel) {
        SONG_ID = `in`.readLong()
        title = `in`.readString()
        lyrics = `in`.readString()
        writers = `in`.readString()
        ytLink = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(SONG_ID)
        dest.writeString(title)
        dest.writeString(lyrics)
        dest.writeString(writers)
        dest.writeString(ytLink)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Song> = object : Creator<Song> {
            override fun createFromParcel(`in`: Parcel): Song {
                return Song(`in`)
            }

            override fun newArray(size: Int): Array<Song?> {
                return arrayOfNulls(size)
            }
        }
    }
}