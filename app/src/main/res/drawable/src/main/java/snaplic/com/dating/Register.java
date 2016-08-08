package snaplic.com.dating;

import android.content.Intent;
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

import org.json.JSONObject;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(Login.mSocket.connected())
        {}
        Button registerButton=(Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editUsername=(EditText)findViewById(R.id.registerUsername);
                EditText editPassword=(EditText)findViewById(R.id.registerPassword);
                EditText confirmPassword=(EditText)findViewById(R.id.registerConfirm);
                EditText firstname=(EditText)findViewById(R.id.registerFirstname);
                EditText lastname=(EditText)findViewById(R.id.registerLastname);
                if(!editPassword.getText().toString().equals(confirmPassword.getText().toString()))
                    Snackbar.make(editUsername,"Passwords do not match",Snackbar.LENGTH_SHORT).show();

                String username= editUsername.getText().toString();
                String password=editPassword.getText().toString();
                Log.d("Username and password",username+" "+password);
                JSONObject register=new JSONObject();
                try{
                    register.put("Username",username);
                    register.put("Password",password);
                    register.put("Firstname",firstname.getText().toString());
                    register.put("Lastname",lastname.getText().toString());


                }
                catch(Exception e)
                {
                    Log.d("JSON",e.toString());
                }

                //editPassword.setText("");
                //editUsername.setText("");
                Login.mSocket.emit("register",register.toString());
            }
        });



        Emitter.Listener regisResult= new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("resultRegis","event fired");
                            String data = (String) args[0];
                            if(data.equals("Success:1"))
                            {
                                Intent intent=new Intent(getApplication(),Login.class);
                                startActivity(intent);


                            }
                            else
                            {
                                Snackbar snackbar=Snackbar.make((EditText)findViewById(R.id.loginUsername),"Email exists",Snackbar.LENGTH_SHORT);
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

        Login.mSocket.on("regis",regisResult);





    }

}
