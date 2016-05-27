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
public class SuperheroListAdapter extends RecyclerView.Adapter<SuperheroListAdapter.MyViewHolder>{
    private final String TAG="SUPERHERO_LIST_ADAPTER";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SuperheroItem> superhero_items;

    public SuperheroListAdapter(Context context, ArrayList<SuperheroItem> superhero_items) {
        this.context= context;
        inflater = LayoutInflater.from(context);
        this.superhero_items = superhero_items;
    }

    @Override
    public int getItemCount() {
        return superhero_items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.superhero_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        SuperheroItem current = superhero_items.get(position);
        holder.hero_name.setText(current.getName());
        if(current.getImage().equals("http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg")){
            holder.hero_img.setVisibility(View.GONE);
        }
        else {
            holder.hero_img.setVisibility(View.VISIBLE);
            holder.hero_img.setImageUrl(current.getImage(), imageLoader);
        }

    }





    class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView hero_name;
        VolleyImageView hero_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            hero_name = (TextView) itemView.findViewById(R.id.txt_superhero_name);
            hero_img = (VolleyImageView)itemView.findViewById(R.id.icon);
        }

    }


}
