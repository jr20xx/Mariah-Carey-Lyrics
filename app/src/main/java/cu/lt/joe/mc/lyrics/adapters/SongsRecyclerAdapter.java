package cu.lt.joe.mc.lyrics.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cu.lt.joe.mc.lyrics.activities.BaseActivity;
import cu.lt.joe.mc.lyrics.databinding.SongItemLayoutBinding;
import cu.lt.joe.mc.lyrics.models.Song;

public class SongsRecyclerAdapter extends RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder>
{
    private final BaseActivity baseActivity;
    private final ArrayList<Song> songs;
    private final OnSongClickedListener onSongClickedListener;

    public SongsRecyclerAdapter(BaseActivity baseActivity, ArrayList<Song> songs, OnSongClickedListener onSongClickedListener)
    {
        this.baseActivity = baseActivity;
        this.songs = songs;
        this.onSongClickedListener = onSongClickedListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new SongViewHolder(SongItemLayoutBinding.inflate(LayoutInflater.from(baseActivity), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position)
    {
        holder.bindSong(songs.get(position));
    }

    @Override
    public int getItemCount()
    {
        return songs.size();
    }

    public interface OnSongClickedListener
    {
        void onSongClicked(Song clickedSong);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder
    {
        private final SongItemLayoutBinding binding;
        private Song boundSong;

        public SongViewHolder(@NonNull SongItemLayoutBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindSong(Song song)
        {
            this.boundSong = song;
            this.binding.setSong(boundSong);
            this.binding.setActivity(baseActivity);
            this.binding.executePendingBindings();
            this.binding.getRoot().setOnClickListener(v -> onSongClickedListener.onSongClicked(song));
        }
    }
}
