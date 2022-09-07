package com.tp.musicapp_project.OtherClasses.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tp.musicapp_project.GenerateData.SongData;
import com.tp.musicapp_project.R;

import java.util.ArrayList;

public class AdapterMood extends RecyclerView.Adapter<AdapterMood.PhoneViewHold>  {

    ArrayList<SongData> popularLocations;
    final private ListItemClickListener mOnClickListener;

    public AdapterMood(ArrayList<SongData> popularLocations, ListItemClickListener listener) {
        this.popularLocations = popularLocations;
        mOnClickListener = listener;
    }

    @NonNull

    @Override
    public PhoneViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlistcardtemplate, parent, false);
        return new PhoneViewHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHold holder, int position) {
        SongData SongData = popularLocations.get(position);
//        holder.image.setImageResource(AdapterHelper.getImage());
        Picasso.get().load(SongData.getSongImageLink()).into(holder.image);
        holder.title.setText(SongData.getSongTitle());
        holder.image.setContentDescription(SongData.getId());
        holder.image.setId(position);
    }

    @Override
    public int getItemCount() {
        return popularLocations.size();
    }

    public interface ListItemClickListener {
        void onmoodListClick(int clickedItemIndex);
    }

    public class PhoneViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        RelativeLayout relativeLayout;


        public PhoneViewHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //hooks

            image = itemView.findViewById(R.id.img_Song);
            title = itemView.findViewById(R.id.txt_songTitle);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onmoodListClick(clickedPosition);
        }
    }

}
