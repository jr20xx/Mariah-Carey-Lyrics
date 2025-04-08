package cu.lt.joe.mc.lyrics.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cu.lt.joe.mc.lyrics.activities.BaseActivity
import cu.lt.joe.mc.lyrics.activities.SongsActivity
import cu.lt.joe.mc.lyrics.adapters.AlbumRecyclerAdapter.AlbumViewHolder
import cu.lt.joe.mc.lyrics.databinding.AlbumItemLayoutBinding
import cu.lt.joe.mc.lyrics.models.Album

class AlbumRecyclerAdapter(private val activity: BaseActivity, private val albums: ArrayList<Album>) :
    RecyclerView.Adapter<AlbumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(AlbumItemLayoutBinding.inflate(LayoutInflater.from(activity), parent, false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindAlbum(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    inner class AlbumViewHolder(private val binding: AlbumItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var boundAlbum: Album? = null
        fun bindAlbum(album: Album?) {
            boundAlbum = album
            binding.album = boundAlbum
            binding.activity = activity
            binding.root.setOnClickListener {
                activity.startActivity(Intent(activity, SongsActivity::class.java).putExtra("album", boundAlbum))
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            binding.executePendingBindings()
        }
    }
}