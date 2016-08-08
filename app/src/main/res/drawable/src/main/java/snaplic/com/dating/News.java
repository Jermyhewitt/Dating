package snaplic.com.dating;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 *
 * create an instance of this fragment.
 */
public class News extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public News() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.news_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter= new NewsAdapter(new String[]{"UploaderName","UploaderName","UploaderName","UploaderName","UploaderName","UploaderName","UploaderName","UploaderName","UploaderName","UploaderName"});

        mRecyclerView.setAdapter(mAdapter);

        return rootView;

    }
}


class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>
{
    String[] posts;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextview,timeStamp;
        public ImageView mImageView,uploader;
        public View mView;
        public ViewHolder(View view)
        {
            super(view);
            mView=view;
            mImageView=(ImageView)view.findViewById(R.id.news_item);
            mTextview=(TextView)view.findViewById(R.id.news_title);
            uploader=(ImageView)view.findViewById(R.id.uploader);
            timeStamp=(TextView)view.findViewById(R.id.timeStamp);


        }
    }


    public NewsAdapter(String posts[])
    {
        this.posts=posts;
    }

    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.newscarditem,parent,false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        Glide.with(viewHolder.uploader.getContext())
                .load(R.drawable.cheese_1)
                .fitCenter()
                .into(viewHolder.uploader);

        viewHolder.mTextview.setText(posts[position]);
        viewHolder.timeStamp.setText("4/7/2016");

        Glide.with(viewHolder.mImageView.getContext())
                .load(R.drawable.cheese_1)
                .fitCenter()
                .into(viewHolder.mImageView);



    }

    public int getItemCount()
    {
        return posts.length;
    }


}
