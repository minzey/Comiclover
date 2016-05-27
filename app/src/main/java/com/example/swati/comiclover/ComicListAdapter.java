package com.example.swati.comiclover;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.swati.comiclover.Networking.AppController;
import com.example.swati.comiclover.Networking.VolleyImageView;

import java.util.ArrayList;

/**
 * Created by Swati on 25-05-2016.
 */
public class ComicListAdapter extends RecyclerView.Adapter<ComicListAdapter.MyViewHolder>{
    private final String TAG="SUPERHERO_LIST_ADAPTER";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ComicItem> comic_items;

    public ComicListAdapter(Context context, ArrayList<ComicItem> comic_items) {
        this.context= context;
        inflater = LayoutInflater.from(context);
        this.comic_items = comic_items;
    }

    @Override
    public int getItemCount() {
        return comic_items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comic_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ComicItem current = comic_items.get(position);
        holder.comic_title.setText(current.getTitle());
        if(current.getDes().equals("null")){
            holder.comic_des.setVisibility(View.GONE);
        }
        else{
            holder.comic_des.setText(current.getDes());
        }

        holder.comic_img.setImageUrl(current.getImage(), imageLoader);

    }





    class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView comic_title;
        TextView comic_des;
        VolleyImageView comic_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            comic_title = (TextView) itemView.findViewById(R.id.txt_comic_title);
            comic_des = (TextView) itemView.findViewById(R.id.txt_comic_des);
            comic_img = (VolleyImageView)itemView.findViewById(R.id.comiccover);
        }

    }


}
