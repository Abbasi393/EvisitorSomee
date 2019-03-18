package com.somee.evisitor.evisitorsomee;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtUsername,edtPassword;
    private String apiUrl;
    private String TAG = LoginActivity.class.getSimpleName();
    private Boolean is_authantic_visitor=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);*/
        /*setSupportActionBar(toolbar);*/

        btnLogin = findViewById(R.id.btnLogin);
        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /*Toast.makeText(MainActivity.this, "Login Successful"+ edtUsername.getText()+" "+edtPassword.getText(), Toast.LENGTH_LONG).show();*/
                apiUrl="/*http://evisitor.somee.com/Visitor/GetVerification?username"+edtUsername.getText()+"&password=@"+edtPassword.getText()+"*/";
                if(edtUsername.getText()!=null && edtPassword.getText()!=null){
                    new GetContacts().execute();
                    /*Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();*/
                }
            }
        });

    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //String url = "http://evisitor.somee.com/Visitor1/GetVerification??username="+edtUsername.getText()+"&password=@"+edtPassword.getText();
            String url = "http://evisitor.somee.com/Visitor1/GetVerification?username="+edtUsername.getText()+"&password="+edtPassword.getText();
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String code =jsonObj.getString("code");
                    String cnic_no=jsonObj.getString("cnic_no");
                    String visitor_image=jsonObj.getString("visitor_image");
                    String name=jsonObj.getString("name");
                    is_authantic_visitor=jsonObj.getBoolean("is_authantic_visitor");
                    //Continue next process

                    if(is_authantic_visitor){
                        //Toast.makeText(LoginActivity.this, "Login Successful \n Username="+ edtUsername.getText(), Toast.LENGTH_LONG).show();
                        Intent main_activity_intent =new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main_activity_intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Failed for User = "+ edtUsername.getText(), Toast.LENGTH_SHORT).show();
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
