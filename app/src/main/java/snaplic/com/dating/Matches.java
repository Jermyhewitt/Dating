package snaplic.com.dating;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.

 * create an instance of this fragment.
 */
public class Matches extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Matches()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("Container","the number of children "+container.getChildCount());
        View rootView= inflater.inflate(R.layout.fragment_matches, container, false);
        mRecyclerView=(RecyclerView) rootView.findViewById(R.id.matches_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter= new EncounterAdapter(new String[]{"This is the line that shows that it works","is","where","it","alll"});
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}

class EncounterAdapter extends RecyclerView.Adapter<EncounterAdapter.ViewHolder>
{
    private String[] data;



    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextview;
        public ViewHolder(View view)
        {
            super(view);
            mTextview=(TextView)view.findViewById(R.id.adaptText);
        }
    }

    public EncounterAdapter(String[] data)
    {
        this.data=data;
    }

    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt,parent,false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position)
        {
            viewHolder.mTextview.setText(data[position]);
        }




    public int getItemCount()
    {
        return data.length;
    }
}

