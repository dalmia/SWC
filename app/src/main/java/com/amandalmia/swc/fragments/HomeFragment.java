package com.amandalmia.swc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amandalmia.swc.R;
import com.amandalmia.swc.storage.SQLiteHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private ScheduleAdapter mAdapter;
    SQLiteHandler db;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.schedule);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        ArrayList<HashMap<String, String>> schedule = db.getSchedule(outFormat.format(calendar.getTime()));
        if(schedule.size()>0){
            (view.findViewById(R.id.empty_view)).setVisibility(View.GONE);
        }
        mAdapter = new ScheduleAdapter(schedule, getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


        Context context;
        private ArrayList<HashMap<String, String>> mDataset;
        RecyclerView mRecyclerView;

        /**
         * Custom onClickListener as recycler view does not support onCLickListener by default.
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView time, subject, venue;

            public ViewHolder(View v) {
                super(v);
                time = (TextView) v.findViewById(R.id.class_time);
                subject = (TextView) v.findViewById(R.id.dashboard_subject_name);
                venue = (TextView) v.findViewById(R.id.venue);
            }
        }

        public void add(int position, HashMap<String, String> item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void swap(ArrayList<HashMap<String, String>> newData) {
            mDataset.clear();
            mDataset.addAll(newData);
            notifyDataSetChanged();
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ScheduleAdapter(ArrayList<HashMap<String, String>> noteDataset, Context context, RecyclerView mRecyclerView) {
            mDataset = noteDataset;
            this.mRecyclerView = mRecyclerView;
            this.context = context;

        }

        @Override
        public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final HashMap<String, String> person = mDataset.get(position);
            holder.time.setText(person.get("time"));
            holder.subject.setText(person.get("subject"));
            holder.venue.setText(person.get("venue"));

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}
