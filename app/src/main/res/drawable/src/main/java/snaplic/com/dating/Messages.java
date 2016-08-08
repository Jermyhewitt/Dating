package snaplic.com.dating;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class Messages extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Messages() {
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
        View rootView =inflater.inflate(R.layout.fragment_messages,container,false);
        mRecyclerView=(RecyclerView) rootView.findViewById(R.id.messages_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter= new MessagesAdapter(getActivity(),new String[]{"Jermaine","Dillon","Chris","Cruze","Hevon","Towanna","shara","Camoy","Tricel"});
        mRecyclerView.setAdapter(mAdapter);
        return rootView;

    }
}


class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>
{
    //change data after message class created
    private String[] data;
    private int mBackground;
    private final TypedValue mTypedValue = new TypedValue();
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView senderPic;
        public TextView senderName, unread, lastMessage, messageTime;
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            senderPic = (ImageView) view.findViewById(R.id.messagesPicID);
            senderName = (TextView) view.findViewById(R.id.messagesSender);
            unread = (TextView) view.findViewById(R.id.messagesUnread);
            lastMessage = (TextView) view.findViewById(R.id.messagesLast);
            messageTime = (TextView) view.findViewById(R.id.messagesTime);
        }
    }

    public MessagesAdapter(Context context,String[] data)
    {
        this.context=context;
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground,mTypedValue,true);
        mBackground=mTypedValue.resourceId;
        this.data=data;
    }

    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view_item,parent,false);
        v.setBackgroundResource(mBackground);
        return new ViewHolder(v);
    }


    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.senderName.setText(data[position]);

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                context.startActivity(new Intent(context,Message.class));
            }



            });


    }

    public int getItemCount()
    {
        return data.length;
    }
}












