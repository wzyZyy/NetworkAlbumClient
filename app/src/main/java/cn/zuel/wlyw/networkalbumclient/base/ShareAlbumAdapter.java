package cn.zuel.wlyw.networkalbumclient.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShareAlbumAdapter extends RecyclerView.Adapter<ShareAlbumAdapter.ViewHolder> {

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

        ViewHolder(View view) {
            super(view);
            albumView = view;
            albumImage = view.findViewById(R.id.aItemImage);
            albumName = view.findViewById(R.id.aItemName);
            albumTime = view.findViewById(R.id.aItemTime);
        }
    }

    public ShareAlbumAdapter(List<Album> albumList, IndexActivity indexActivity) {
        this.albumList = albumList;
        this.indexActivity = indexActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 将album_item布局加载进来
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_album_item, parent, false);
        // 把加载出来的布局传入到构造函数
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

                // 查看该相册下的所有照片，启动ShareImageActivity由：IndexActivity发起
                indexActivity.viewShareImageByAlbum(album.getA_id());
            }
        });
        // 将ViewHolder的实例返回
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 对RecyclerView子项的数据进行赋值
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
