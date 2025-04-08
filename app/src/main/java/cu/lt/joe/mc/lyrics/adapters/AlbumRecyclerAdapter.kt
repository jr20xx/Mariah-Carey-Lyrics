package cu.lt.joe.mc.lyrics.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.mc.lyrics.activities.BaseActivity;
import cu.lt.joe.mc.lyrics.activities.SongsActivity;
import cu.lt.joe.mc.lyrics.databinding.AlbumItemLayoutBinding;
import cu.lt.joe.mc.lyrics.models.Album;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.AlbumViewHolder>
{
    private final BaseActivity activity;
    private final ArrayList<Album> albums;

    public AlbumRecyclerAdapter(BaseActivity activity, ArrayList<Album> albums)
    {
        this.activity = activity;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new AlbumViewHolder(AlbumItemLayoutBinding.inflate(LayoutInflater.from(activity), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position)
    {
        holder.bindAlbum(albums.get(position));
    }

    @Override
    public int getItemCount()
    {
        return albums.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder
    {
        private final AlbumItemLayoutBinding binding;
        private Album boundAlbum;

        public AlbumViewHolder(@NonNull AlbumItemLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindAlbum(Album album)
        {
            boundAlbum = album;
            binding.setAlbum(boundAlbum);
            binding.setActivity(activity);
            binding.getRoot().setOnClickListener(v ->
            {
                activity.startActivity(new Intent(activity, SongsActivity.class).putExtra("album", boundAlbum));
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
            binding.executePendingBindings();
        }
    }
}