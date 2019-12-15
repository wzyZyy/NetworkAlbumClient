package cn.zuel.wlyw.networkalbumclient.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.ImageActivity;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<Image> imageList;
    private ImageActivity imageActivity;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    static class ViewHolder extends RecyclerView.ViewHolder {
        View imageView;
        ImageView imageImage;
        TextView imageName;
        TextView imageTime;
        Button deleteImage;

        ViewHolder(View view) {
            super(view);
            imageView = view;
            imageImage = view.findViewById(R.id.iItemImage);
            imageName = view.findViewById(R.id.iItemName);
            imageTime = view.findViewById(R.id.iItemTime);
            deleteImage = view.findViewById(R.id.iDeleteItem);
        }
    }

    public ImageAdapter(List<Image> imageList, ImageActivity imageActivity) {
        this.imageList = imageList;
        this.imageActivity = imageActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = imageList.get(position);
                Toast.makeText(v.getContext(), image.getI_id() + " " + image.getI_name(), Toast.LENGTH_SHORT).show();
                // 跳转到PreviewActivity，预览大图，由ImageActivity发起
                imageActivity.previewImage(image.getI_path());
            }
        });
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = imageList.get(position);
                Toast.makeText(v.getContext(), "delete " + image.getI_id(), Toast.LENGTH_SHORT).show();
//            imageActivity.deleteImage(image.getI_id());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = imageList.get(position);
        Glide.with(imageActivity)
                .load(MainConfig.REQUEST_URL + image.getI_path())
                .placeholder(R.drawable.loading)
                .into(holder.imageImage);
        holder.imageName.setText(image.getI_name());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String time = sdf.format(image.getI_updatetime());
        holder.imageTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
