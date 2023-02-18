package com.optim.bassit.ui.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.optim.bassit.R;
import com.optim.bassit.utils.OptimTools;

public class ImgGridViewAdapter extends BaseAdapter {
    private final Context context;
    private boolean has_delete;

    public List<String> getmData() {
        return mData;
    }

    private List<String> mData;

    int itemView_id;

    public ImgGridViewAdapter(Context ctx, int res, boolean has_delete) {
        this.context = ctx;
        this.has_delete = has_delete;
        this.mData = new ArrayList<>();
        itemView_id = res;
    }


    public void addOne(String media) {

        mData.add(media);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View vw2, ViewGroup parent) {
        View vw = LayoutInflater.from(context).inflate(itemView_id, parent, false);
        String path = getItem(position).toString();
        ImageView img = vw.findViewById(R.id.image);
        ImageButton btn = vw.findViewById(R.id.btn_delete_image);

        if (has_delete) {
            if (path.toLowerCase().startsWith("http")) {
                OptimTools.getPicasso(path).into(img);
            } else {

                File imgFile = new File(path);

                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                }
            }
            btn.setOnClickListener(view -> {
                mData.remove(position);
                notifyDataSetChanged();
            });

        } else {
            OptimTools.getPicasso(path).into(img);
            btn.setVisibility(View.GONE);

            vw.setOnClickListener(view -> OptimTools.previewPhoto(path, vw.getContext()));
        }


        return vw;
    }


}
