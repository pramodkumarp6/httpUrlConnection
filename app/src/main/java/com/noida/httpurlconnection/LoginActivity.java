package com.noida.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.noida.httpurlconnection.R;
import com.noida.httpurlconnection.app.Contants;
import com.noida.httpurlconnection.app.RequestHandler;
import com.noida.httpurlconnection.model.SharedPrefManager;
import com.noida.httpurlconnection.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private Button register, buttonlogin;
    private EditText uemail;
    private EditText pass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        register = findViewById(R.id.textViewRegister1);
        buttonlogin = findViewById(R.id.buttonLogin);
        uemail = findViewById(R.id.editTextemail);
        pass = findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }

    private void userLogin() {
        final String email = uemail.getText().toString();
        final String password = pass.getText().toString();

        progressDialog.setMessage("loding");
        progressDialog.show();
      class Userlogin extends AsyncTask<Void,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                /*   //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    //creating a new user object
                    User user = new User(
                            userJson.getInt("id"),
                            userJson.getString("name"),
                            userJson.getString("email"),
                            userJson.getString("gender")
                    );*/


                   /* SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                    //starting the profile activity
                    finish();*/
                  Intent intent= new Intent(LoginActivity.this, ProfileAcitivity.class);
                  startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(Contants.URL_LOGIN, params);
        }
    }

          Userlogin ul = new Userlogin();
        ul.execute();
}
}