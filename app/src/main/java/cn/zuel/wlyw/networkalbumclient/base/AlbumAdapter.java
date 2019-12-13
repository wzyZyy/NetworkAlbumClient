package cn.zuel.wlyw.networkalbumclient.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.IndexActivity;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> albumList;
    private IndexActivity indexActivity;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    static class ViewHolder extends RecyclerView.ViewHolder {
        View albumView;
        ImageView albumImage;
        TextView albumName;
        TextView albumTime;
        Button deleteAlbum;

        ViewHolder(View view) {
            super(view);
            albumView = view;
            albumImage = view.findViewById(R.id.itemImage);
            albumName = view.findViewById(R.id.itemName);
            albumTime = view.findViewById(R.id.itemTime);
            deleteAlbum = view.findViewById(R.id.deleteItem);
        }
    }

    public AlbumAdapter(List<Album> albumList, IndexActivity indexActivity) {
        this.albumList = albumList;
        this.indexActivity = indexActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.albumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Album album = albumList.get(position);
                Toast.makeText(v.getContext(), album.getA_id() + " " + album.getA_name(), Toast.LENGTH_SHORT).show();
                indexActivity.viewImageByAlbum(album.getA_id());
            }
        });
        holder.deleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Album album = albumList.get(position);
                Toast.makeText(v.getContext(), "delete " + album.getA_id(), Toast.LENGTH_SHORT).show();
                indexActivity.deleteAlbum(album.getA_id());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.albumImage.setImageResource(R.drawable.file_icon);
        holder.albumName.setText(album.getA_desc());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String time = sdf.format(album.getA_updatetime());
        holder.albumTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
