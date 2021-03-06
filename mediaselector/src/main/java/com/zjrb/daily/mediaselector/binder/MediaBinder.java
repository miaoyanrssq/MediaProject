package com.zjrb.daily.mediaselector.binder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zjrb.daily.mediaselector.R;
import com.zjrb.daily.mediaselector.config.MediaSelectionConfig;
import com.zjrb.daily.mediaselector.entity.MediaEntity;
import com.zjrb.daily.mediaselector.listener.OnItemClickListener;

import me.drakeet.multitype.ItemViewBinder;


public class MediaBinder extends ItemViewBinder<MediaEntity, MediaBinder.MediaHolder> {

    private final static int DURATION = 450;
    CompoundButton.OnCheckedChangeListener listener;
    OnItemClickListener onItemClickListener;
    MediaSelectionConfig config;


    class MediaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView iv_picture;
        CheckBox check_box;
        public MediaHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            check_box = itemView.findViewById(R.id.check_box);
            check_box.setOnCheckedChangeListener(listener);
            check_box.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == itemView){
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            }
            else if(view == check_box){
                if(config.zoomAnim) {
                    if (check_box.isChecked()) {
                        zoom(iv_picture);
                    } else {
                        disZoom(iv_picture);
                    }
                }
            }
        }
    }


    public MediaBinder(CompoundButton.OnCheckedChangeListener listener, OnItemClickListener onItemClickListener, MediaSelectionConfig config) {
        this.listener = listener;
        this.onItemClickListener = onItemClickListener;
        this.config = config;
    }

    @NonNull
    @Override
    protected MediaHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_local_media_select, parent, false);
        return new MediaHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull MediaHolder holder, @NonNull MediaEntity item) {
        if(TextUtils.isEmpty(item.getThumbnail())) {
            Glide.with(holder.iv_picture.getContext()).load(item.getPath()).into(holder.iv_picture);
        }else{
            Glide.with(holder.iv_picture.getContext()).load(item.getThumbnail()).into(holder.iv_picture);
        }
        holder.check_box.setTag(null);
        holder.check_box.setChecked(item.isSelected());
        holder.check_box.setTag(item);
        if(!item.isSelected() && config.zoomAnim){
            disZoom(holder.iv_picture);
        }

    }



    private void zoom(ImageView iv_img) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(iv_img, "scaleX", 1f, 1.12f),
                    ObjectAnimator.ofFloat(iv_img, "scaleY", 1f, 1.12f)
            );
            set.setDuration(DURATION);
            set.start();
    }

    private void disZoom(ImageView iv_img) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(iv_img, "scaleX", 1.12f, 1f),
                    ObjectAnimator.ofFloat(iv_img, "scaleY", 1.12f, 1f)
            );
            set.setDuration(DURATION);
            set.start();
    }

}
