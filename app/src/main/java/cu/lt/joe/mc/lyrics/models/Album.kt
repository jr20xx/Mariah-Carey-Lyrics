package cu.lt.joe.mc.lyrics.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import cu.lt.joe.mc.lyrics.R

class Album : Parcelable {
    val ALBUM_ID: Long

    @JvmField
    val title: String?

    @JvmField
    val recording_label: String?

    @JvmField
    val wikiLink: String?

    @JvmField
    val songs: ArrayList<Song>?
    private val year: Int

    constructor(ALBUM_ID: Long, title: String?, recording_label: String?, songs: ArrayList<Song>?, year: Int, wikiLink: String?) {
        this.ALBUM_ID = ALBUM_ID
        this.title = title
        this.recording_label = recording_label
        this.songs = songs
        this.year = year
        this.wikiLink = wikiLink
    }

    protected constructor(`in`: Parcel) {
        ALBUM_ID = `in`.readLong()
        title = `in`.readString()
        recording_label = `in`.readString()
        songs = `in`.createTypedArrayList(Song.CREATOR)
        year = `in`.readInt()
        wikiLink = `in`.readString()
    }

    val albumDrawableResourceId: Int
        get() = when (ALBUM_ID.toInt()) {
            1 -> R.drawable.mc
            2 -> R.drawable.emotions
            3 -> R.drawable.music_box
            4 -> R.drawable.merry_christmas
            5 -> R.drawable.daydream
            6 -> R.drawable.butterfly
            7 -> R.drawable.rainbow
            8 -> R.drawable.glitter
            9 -> R.drawable.charmbracelet
            10 -> R.drawable.emancipation
            11 -> R.drawable.emcc
            12 -> R.drawable.memoirs
            13 -> R.drawable.merry_christmas_2
            14 -> R.drawable.me_i_am_mariah
            15 -> R.drawable.caution
            else -> R.drawable.splash
        }

    fun getYear(): String {
        return year.toString() + ""
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(ALBUM_ID)
        dest.writeString(title)
        dest.writeString(recording_label)
        dest.writeTypedList(songs)
        dest.writeInt(year)
        dest.writeString(wikiLink)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Album> = object : Creator<Album> {
            override fun createFromParcel(`in`: Parcel): Album {
                return Album(`in`)
            }

            override fun newArray(size: Int): Array<Album?> {
                return arrayOfNulls(size)
            }
        }

        @JvmStatic
        @BindingAdapter("android:src")
        fun setAlbumDrawable(iv: ImageView?, resourceId: Int) {
            Glide.with(iv!!).load(resourceId).into(iv)
        }

        @JvmStatic
        @BindingAdapter("android:roundSrc")
        fun setAlbumRoundDrawable(iv: ImageView?, resourceId: Int) {
            Glide.with(iv!!).load(resourceId).transform(CircleCrop()).into(iv)
        }
    }
}