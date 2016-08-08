package snaplic.com.dating;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URL;

public class Login extends AppCompatActivity {

    public static Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=this.getPreferences(Context.MODE_PRIVATE);
        String pref=preferences.getString(getString(R.string.loginStatus),null);
        if(pref==null)
        {
            Log.d("pref","pref "+
                    preferences.getString(getString(R.string.loginUsername),null)+
                    " pass"+preferences.getString(getString(R.string.loginPassword),null));

        }
        else
        {
            Log.d("pref","pref "+
                    preferences.getString(getString(R.string.loginUsername),null)+
                    " pass"+preferences.getString(getString(R.string.loginPassword),null));
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);

        }
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button loginButton=(Button)findViewById(R.id.loginButton);

        try {
            mSocket = IO.socket((new URL("http","192.168.0.4",3000,"/")).toURI());
            Log.d("URI",(new URL("http","192.168.0.4",3000,"/")).toString());
        } catch (Exception e) {
            Log.e("URIEXCEPTION","error in the socket ");
        }

        mSocket.connect();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editUsername=(EditText)findViewById(R.id.loginUsername);
                EditText editPassword=(EditText)findViewById(R.id.loginPassword);
                String username= editUsername.getText().toString();
                String password=editPassword.getText().toString();
                SharedPreferences sharedPreferences=Login.this.getPreferences(Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(getString(R.string.loginUsername),username).commit();
                sharedPreferences.edit().putString(getString(R.string.loginPassword),password).commit();

                Log.d("Username and password",username+" "+password);
                JSONObject login=new JSONObject();
                try{
                    login.put("Username",username);
                    login.put("Password",password);

                }
                catch(Exception e)
                {
                    Log.d("JSON",e.toString());
                }

                editPassword.setText("");
                editUsername.setText("");
                mSocket.emit("login",login.toString());
            }
        });

       TextView register=(TextView) findViewById(R.id.loginRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplication(),Register.class);
                startActivity(intent);

            }
        });



        Emitter.Listener loginResult= new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("resultRegis","event fired");
                            String data = (String) args[0];
                            Snackbar snackbar;

                            if(data.equals("1"))
                            {

                                SharedPreferences sharedPreferences=Login.this.getPreferences(Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString(getString(R.string.loginStatus),"logged_in").commit();
                                Intent intent=new Intent(getApplication(),MainActivity.class);
                                startActivity(intent);


                            }
                            else if(data.equals("2"))
                            {
                                snackbar=Snackbar.make((EditText)findViewById(R.id.loginUsername),"Password Incorrect",Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                                ((TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).
                                        setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                snackbar.show();
                            }
                            else {
                                snackbar=Snackbar.make((EditText)findViewById(R.id.loginUsername),"Username does not exist",Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                                ((TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).
                                        setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                                snackbar.show();
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("data error",e.toString());
                        }
                    }
                });

            }
        };

        mSocket.on("loginResult",loginResult);


    }

}
