package com.amandalmia.swc.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amandalmia.swc.app.CommonUtilities;
import com.amandalmia.swc.R;
import com.amandalmia.swc.storage.SQLiteHandler;
import com.amandalmia.swc.helper.WakeLocker;
import com.amandalmia.swc.activities.AnnouncementDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.amandalmia.swc.app.APIParameters.DESCRIPTION;
import static com.amandalmia.swc.app.APIParameters.TITLE;

public class AnnouncementFragment extends Fragment {

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            Log.d("Message", newMessage);
            try {
                JSONObject json = new JSONObject(newMessage);
                HashMap<String, String> announcement = new HashMap<>();
                announcement.put(TITLE, json.getString(TITLE));
                announcement.put(DESCRIPTION, json.getString(DESCRIPTION));
                mAdapter.add(mAdapter.getItemCount(), announcement);
                emptyAnnouncements.setVisibility(View.GONE);
                mLayoutManager.scrollToPosition(mAdapter.getItemCount()-1);
                WakeLocker.release();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private AnnouncementsAdapter mAdapter;
    SQLiteHandler db;
    LinearLayout emptyAnnouncements;

    public AnnouncementFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.announcement_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.announcements);
        mLayoutManager = new LinearLayoutManager(getActivity());
        emptyAnnouncements = (LinearLayout) view.findViewById(R.id.empty_announcements);

        db = new SQLiteHandler(getActivity().getApplicationContext());
        getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_NOTICE_BOARD));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setHasFixedSize(false);
        ArrayList<HashMap<String, String>> aboutBranch = db.getAnnouncements();
        if (aboutBranch.size() > 0) {
            emptyAnnouncements.setVisibility(View.GONE);
        }
        mAdapter = new AnnouncementsAdapter(aboutBranch, getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager.scrollToPosition(mAdapter.getItemCount()-1);
        return view;
    }

    public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {


        Context context;
        private ArrayList<HashMap<String, String>> mDataset;
        RecyclerView mRecyclerView;

        /**
         * Custom onClickListener as recycler view does not support onCLickListener by default.
         */
        private final View.OnClickListener mOnClickListener = new RecyclerViewOnClickListener();

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title, icon, description;

            public ViewHolder(View v) {
                super(v);
                description = (TextView) v.findViewById(R.id.note_description);
                title = (TextView) v.findViewById(R.id.note_title);
                icon = (TextView) v.findViewById(R.id.note_icon);
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
        public AnnouncementsAdapter(ArrayList<HashMap<String, String>> noteDataset, Context context, RecyclerView mRecyclerView) {
            mDataset = noteDataset;
            this.mRecyclerView = mRecyclerView;
            this.context = context;

        }

        @Override
        public AnnouncementsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_list_item, parent, false);
            v.setOnClickListener(mOnClickListener);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final HashMap<String, String> person = mDataset.get(position);
            holder.title.setText(person.get(TITLE));
            holder.description.setText(person.get(DESCRIPTION));
            holder.icon.setText(person.get(TITLE).trim().substring(0, 1).toUpperCase());

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public class RecyclerViewOnClickListener implements View.OnClickListener {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                Intent intent = new Intent(context, AnnouncementDetailActivity.class);
                HashMap<String, String> userDetails = mDataset.get(position);
                Bundle messageValues = new Bundle();
                messageValues.putString(TITLE, userDetails.get(TITLE));
                messageValues.putString(DESCRIPTION, userDetails.get(DESCRIPTION));
                intent.putExtras(messageValues);
                context.startActivity(intent);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}