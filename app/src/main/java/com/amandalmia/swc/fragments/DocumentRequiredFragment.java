package com.amandalmia.swc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amandalmia.swc.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DocumentRequiredFragment extends Fragment {


    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private DocumentsAdapter mAdapter;
    String[] documentsRequired = {
            "1.  One copy of Provisional Seat Allocation letter [to be downloaded from the JoSAA portal].",
            "2.  Original receipt of proof of payment for seat acceptance fee of Rs.45,000 (for GEN, OBC-NCL and Foreign) OR Rs.20,000 (for SC, ST, GEN-PwD, OBC-NCL-PwD, SC-PwD, ST-PwD) in State Bank of India using E-Challan [to be downloaded from the JoSAA portal].\n",
            "3.  Filled-up Undertaking by the candidate [as per <a href=\"http://josaa.nic.in/webinfo/PDFViewer.aspx?page=Undertaking%20by%20Candidate\">Annexure 9</a> given in JoSAA website].\n",
            "4.  Duly certified original medical report [as per <a href=\"http://josaa.nic.in/webinfo/PDFViewer.aspx?page=Medical%20Report\">Annexure 8</a> given in JoSAA website].\n",
            "5.  Two Passport size photographs of the candidate. \n",
            "6.  Original and one photocopy of Admit Card of JEE (Advanced) 2015.\n",
            "7.  Original and one attested photocopy of Birth certificate or equivalent original certificate as proof of date of birth.\n",
            "8.  Original and one attested photocopy of Certificate as proof of Name [to tally with the entry in JoSAA database] (example: Marksheet or Certificate of Xth Exam).\n",
            "9.  Original and one attested photocopy of Category (SC / ST) certificate, if applicable, in the <a href=\"http://josaa.nic.in/webinfo/PDFViewer.aspx?page=FormEC1_SC_ST\">format</a> given in JoSAA website (Issued by competent authority).\n",
            "10. Original and one attested photocopy of Certificate of category of OBC-NCL (central list of NCBC), if applicable, is to be issued by the competent authority in the prescribed <a href=\"http://josaa.nic.in/webinfo/PDFViewer.aspx?page=FormEC2_OBC\">format</a> given in JoSAA website and should clearly mention that the candidate belongs to Non Creamy Layer. The certificate should have been issued after 1st June, 2014.\n",
            "11. Original and one attested photocopy of valid Disability certificate (if applicable) for PwD candidates.\n",
            "12. Original and one photocopy of valid Nationality certificate/document/passport (if applicable) for candidates with nationality as Foreign, OCI and PIO card holders.\n",
            "13. Original and one photocopy of valid Class XII marks sheet.\n",
            "The above are the minimum required documents as prescribed by JoSAA. It is suggested to carry more than just a couple of photographs as they will be needed, a lot. Also keep a digital " +
                    "copy of the same along with that of your signature handy. It will help you later. \n Along with the above original certificates, candidates " +
                    "are advised to bring the following : \n 1. Printout of locked choices and terms and " +
                    "conditions taken from the JoSAA website and duly signed by the candidate.\n 2. " +
                    "One set of self-attested copies of all the documents listed above. The originals will be returned" +
                    " after verification and self-attested copies will be retained by the Reporting Centre."
    };

    public DocumentRequiredFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.document_required_fragment, container, false);
        ((TextView)view.findViewById(R.id.documents)).setMovementMethod(LinkMovementMethod.getInstance());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.data);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        ArrayList<String> documentDetails = new ArrayList<>(Arrays.asList(documentsRequired));
        mAdapter = new DocumentsAdapter(documentDetails, getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {


        Context context;
        private ArrayList<String> mDataset;
        RecyclerView mRecyclerView;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.document_lists);
            }
        }

        public void add(int position, String item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void swap(ArrayList<String> newData) {
            mDataset.clear();
            mDataset.addAll(newData);
            notifyDataSetChanged();
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public DocumentsAdapter(ArrayList<String> noteDataset, Context context, RecyclerView mRecyclerView) {
            mDataset = noteDataset;
            this.mRecyclerView = mRecyclerView;
            this.context = context;

        }

        @Override
        public DocumentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.documents_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String detail = mDataset.get(position);
            holder.title.setText(detail);
            holder.title.setMovementMethod(LinkMovementMethod.getInstance());

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
