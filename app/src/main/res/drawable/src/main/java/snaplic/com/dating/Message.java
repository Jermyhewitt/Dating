package snaplic.com.dating;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.Url;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Message extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private Socket mSocket;
    MessageDbHelper messageHelper;

/*
        Remember to remove the remove rule in the bind view method and find a compatible way
 */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageHelper=new MessageDbHelper(this);

        try {
            mSocket = IO.socket((new URL("http","192.168.0.4",3000,"/Message")).toURI());
            Log.d("URI",(new URL("http","192.168.0.4",3000,"/Message")).toString());
        } catch (Exception e) {
            Log.e("URIEXCEPTION","error in the socket ");
        }


        mSocket.connect();


        toolbar=(Toolbar)findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView=(RecyclerView) findViewById(R.id.message_recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        final ArrayList<MessageObject> list=getMessages();
        mAdapter= new MessageAdapter(this,list);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.smoothScrollToPosition(list.size());





        JSONObject obj= new JSONObject();
        try {
            obj.put("name", this.getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.loginStatus),null));
        }
        catch (Exception e)
        {

        }

        mSocket.emit("new_user",obj.toString());

        ((ImageView)findViewById(R.id.message_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=(EditText)(findViewById(R.id.message_text));
                String message=(String)(editText.getText().toString());
                if(!message.trim().equals(""))
                {

                    final MessageObject messageObject=new MessageObject();
                    messageObject.setSender("Jermaine");
                    messageObject.setContent(message);
                    messageObject.setDestination("Towanna");
                    list.add(messageObject);
                    mAdapter.notifyItemInserted(list.size());
                    Log.d("UIThread","is "+Thread.currentThread());



                    saveMessage(messageObject);
                    JSONObject mess=new JSONObject();
                    try {
                        mess.put("sender", messageObject.getSender());
                        mess.put("receiver",messageObject.getDestination());
                        mess.put("content",messageObject.getContent());
                        mess.put("timeStamp",messageObject.getTimestamp());
                    }
                    catch (Exception e)
                    {

                    }

                    mSocket.emit("chat message",mess.toString());
                    editText.setText("");
                    mRecyclerView.smoothScrollToPosition(list.size());

                }

            }
        });



        Emitter.Listener newMessage= new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String data = (String) args[0];
                            JSONObject newMessage= new JSONObject(data);
                            final MessageObject message=new MessageObject(newMessage.getString("sender"),newMessage.getString("receiver"),newMessage.getString("content"));
                            list.add(message);
                            Log.d("newMessage",message.toString());
                            saveMessage(message);

                            mAdapter.notifyItemInserted(list.size());
                            mRecyclerView.smoothScrollToPosition(list.size());
                            Log.d("test","callback");
                        }
                        catch (Exception e)
                        {
                            Log.e("data error",e.toString());
                        }
                    }
                });

            }
        };

        mSocket.on("chat message",newMessage);


            }



    public void saveMessage(final MessageObject messageObject)
    {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = messageHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(MessageContract.MessageEntry.COLUMN_NAME_MessageContent, messageObject.getContent());
                    values.put(MessageContract.MessageEntry.COLUMN_NAME_Receiver, messageObject.getDestination());
                    values.put(MessageContract.MessageEntry.COLUMN_NAME_Sender, messageObject.getSender());
                    values.put(MessageContract.MessageEntry.COLUMN_NAME_Time, "Just inserted");

                    Log.d("OtherThread","is "+Thread.currentThread());
                    long newRowId = db.insert(
                            MessageContract.MessageEntry.TABLE_NAME,
                            MessageContract.MessageEntry.COLUMN_NAME_NULLABLE,
                            values);
                    Log.d("success", "the row it"+newRowId);

                }
            }).start();
        }
        catch (Error e)
        {
            Log.e("thread",e.toString());
        }
    }

    public ArrayList<MessageObject> getMessages()
    {

        /*
        UNFINISHED
        Remember to use cursor loader here to improve performance
         */
        ArrayList<MessageObject> list=new ArrayList<>();
        SQLiteDatabase reader=messageHelper.getReadableDatabase();
        String[] projection={MessageContract.MessageEntry.COLUMN_NAME_MessageContent, MessageContract.MessageEntry.COLUMN_NAME_Sender};
        String mSelectionClause = null;
        // String mSelectionArgs[] = {};
        //mSelectionArgs[0] = "";
        Cursor c=reader.query(
                MessageContract.MessageEntry.TABLE_NAME,
                projection,
                mSelectionClause,
                null,
                null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            int contentIndex=c.getColumnIndex(MessageContract.MessageEntry.COLUMN_NAME_MessageContent);
            int senderIndex=c.getColumnIndex((MessageContract.MessageEntry.COLUMN_NAME_Sender));
            while (c.moveToNext())
            {
                list.add(new MessageObject(c.getString(senderIndex),"other",c.getString(contentIndex)));
                Log.d("data at "+contentIndex+" "+senderIndex,c.getString(contentIndex));
            }
        }
        return list;
    }



}

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
{
    private ArrayList<MessageObject> data;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public View mView;
        public ImageView imageView;
        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            textView = (TextView) view.findViewById(R.id.message_content);
        }
    }


    public MessageAdapter(Context context, ArrayList data)
    {
        this.data=data;
    }

    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.bubble_message,parent,false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        viewHolder.textView.setText(data.get(position).getContent());
        if (data.get(position).getSender().equals("Jermaine"))
        {
            viewHolder.textView.setBackgroundResource(R.drawable.bubble2);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.textView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        else
        {
            viewHolder.textView.setBackgroundResource(R.drawable.bubble1);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.textView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        }


    }

    public int getItemCount()
    {
        return data.size();
    }




}