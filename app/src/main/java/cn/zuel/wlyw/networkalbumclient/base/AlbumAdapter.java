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
        // 子项最外层布局的实例
        View albumView;
        // 子项
        ImageView albumImage;
        TextView albumName;
        TextView albumTime;
        Button deleteAlbum;

        ViewHolder(View view) {
            super(view);
            albumView = view;
            albumImage = view.findViewById(R.id.aItemImage);
            albumName = view.findViewById(R.id.aItemName);
            albumTime = view.findViewById(R.id.aItemTime);
            deleteAlbum = view.findViewById(R.id.aDeleteItem);
        }
    }

    public AlbumAdapter(List<Album> albumList, IndexActivity indexActivity) {
        this.albumList = albumList;
        this.indexActivity = indexActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 子项最外层布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        /*
        点击相册查看该相册的所有照片
         */
        holder.albumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Album album = albumList.get(position);
                Toast.makeText(v.getContext(), "查看相册：" + album.getA_name(), Toast.LENGTH_SHORT).show();

                // 查看该相册下的所有照片，启动ImageActivity由：IndexActivity发起
                indexActivity.viewImageByAlbum(album.getA_id());
            }
        });
        /*
         * 删除相册
         */
        holder.deleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Album album = albumList.get(position);
                Toast.makeText(v.getContext(), "删除相册：" + album.getA_name(), Toast.LENGTH_SHORT).show();
                // 删除相册
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
