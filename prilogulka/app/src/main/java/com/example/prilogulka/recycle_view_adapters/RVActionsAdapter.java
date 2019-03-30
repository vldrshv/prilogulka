package com.example.prilogulka.recycle_view_adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Video;

import java.util.List;

public class RVActionsAdapter extends RecyclerView.Adapter<RVActionsAdapter.PersonViewHolder>{
        List<Video> videoList;

        public RVActionsAdapter(List<Video> videoList){
            this.videoList = videoList;
        }

        public static class PersonViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView db_id, db_email, db_video_id, db_watch_points, db_watch_date;

            PersonViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv_actions);
                db_id = (TextView)itemView.findViewById(R.id.db_id_actions);
                db_email = (TextView)itemView.findViewById(R.id.db_email_actions);
                db_video_id = (TextView)itemView.findViewById(R.id.db_video_id);
                db_watch_points = (TextView)itemView.findViewById(R.id.db_watch_points);
                db_watch_date = (TextView)itemView.findViewById(R.id.db_watch_date);

            }

        }

        @Override
        public void onBindViewHolder(RVActionsAdapter.PersonViewHolder personViewHolder, int i) {
            personViewHolder.db_id.setText(videoList.get(i).getId()+"");
            personViewHolder.db_email.setText(videoList.get(i).getUserWatched());
            personViewHolder.db_video_id.setText(videoList.get(i).getVideoId());
            personViewHolder.db_watch_points.setText(videoList.get(i).getWatchPoints() + "");
            personViewHolder.db_watch_date.setText(videoList.get(i).getWatchDate());
        }


        @Override
        public RVActionsAdapter.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_actions_presentation, viewGroup, false);
            RVActionsAdapter.PersonViewHolder pvh = new RVActionsAdapter.PersonViewHolder(v);
            return pvh;
        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


}
