package com.amandalmia.swc.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.amandalmia.swc.activities.BranchDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.amandalmia.swc.app.APIParameters.DESCRIPTION;
import static com.amandalmia.swc.app.APIParameters.TITLE;

public class AboutBranchesFragment extends Fragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private BranchesAdapter mAdapter;

    public AboutBranchesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_branches_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.branches);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        List<String> branchNames = Arrays.asList(getResources().getStringArray(R.array.branch_names));
        List<String> branchDetails = Arrays.asList(getResources().getStringArray(R.array.branch_description));
        ArrayList<HashMap<String, String>> aboutBranch = new ArrayList<>();
        for(int i = 0; i<branchNames.size(); i++){
            HashMap<String, String> details = new HashMap<>();
            details.put(TITLE, branchNames.get(i));
            details.put(DESCRIPTION, branchDetails.get(i));
            aboutBranch.add(details);
        }
        mAdapter = new BranchesAdapter(aboutBranch, getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.ViewHolder> {


        Context context;
        private ArrayList<HashMap<String, String>> mDataset;
        RecyclerView mRecyclerView;

        /**
         * Custom onClickListener as recycler view does not support onCLickListener by default.
         */
        private final View.OnClickListener mOnClickListener = new RecyclerViewOnClickListener();

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title, icon;

            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.note_title);
                icon = (TextView) v.findViewById(R.id.icon);
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
        public void swap(ArrayList<HashMap<String, String>> newData){
            mDataset.clear();
            mDataset.addAll(newData);
            notifyDataSetChanged();
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public BranchesAdapter(ArrayList<HashMap<String, String>> noteDataset, Context context, RecyclerView mRecyclerView) {
            mDataset = noteDataset;
            this.mRecyclerView = mRecyclerView;
            this.context = context;

        }

        @Override
        public BranchesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.branches_list_item, parent, false);
            v.setOnClickListener(mOnClickListener);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final HashMap<String, String> person = mDataset.get(position);
            holder.title.setText(person.get(TITLE));
            holder.icon.setText(person.get(TITLE).substring(0, 1).toUpperCase());

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
                Intent intent = new Intent(context, BranchDetailActivity.class);
                HashMap<String, String> userDetails = mDataset.get(position);
                Bundle messageValues = new Bundle();
                messageValues.putString(TITLE, userDetails.get(TITLE));
                messageValues.putString(DESCRIPTION, userDetails.get(DESCRIPTION));
                intent.putExtras(messageValues);
                context.startActivity(intent);
            }
        }

    }
}
