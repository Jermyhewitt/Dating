package snaplic.com.dating;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


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

        mAdapter= new EncounterAdapter(getActivity(),new String[]{"name 1","name2","name3","name4","name5","name6","name7","name8","name9","name10","name11"});
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}

class EncounterAdapter extends RecyclerView.Adapter<EncounterAdapter.ViewHolder>
{
    private String[] data;
    private int mBackground;
    private final TypedValue mTypedValue = new TypedValue();



    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextview;
        public ImageView mImageView;
        public View mView;
        public ViewHolder(View view)
        {
            super(view);
            mView=view;
            mImageView=(ImageView)view.findViewById(R.id.picid);
            mTextview=(TextView)view.findViewById(R.id.adaptText);
        }
    }

    //The background of the listview click is set here
    public EncounterAdapter(Context context,String[] data)
    {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground,mTypedValue,true);
        mBackground=mTypedValue.resourceId;
        this.data=data;
    }

    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt,parent,false);
        v.setBackgroundResource(mBackground);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position)
        {
            viewHolder.mTextview.setText(data[position]);



            viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    Snackbar.make(v, "Clicked item", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }

            });

            Glide.with(viewHolder.mImageView.getContext())
                    .load(R.drawable.cheese_1)
                    .fitCenter()
                    .into(viewHolder.mImageView);
        }




    public int getItemCount()
    {
        return data.length;
    }
}

