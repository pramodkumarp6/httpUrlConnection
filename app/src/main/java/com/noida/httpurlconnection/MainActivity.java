package com.noida.httpurlconnection;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.noida.httpurlconnection.app.Contants;
import com.noida.httpurlconnection.app.RequestHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button register;
    private EditText Email,Pass,Name,School;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Registration");
        register = findViewById(R.id.buttonSignUp);
        Email = findViewById(R.id.editTextEmail);
        Pass = findViewById(R.id.editTextPassword);
        Name = findViewById(R.id.editTextName);
        School = findViewById(R.id.editTextSchool);
        progressDialog = new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    private void register(){
        final String email = Email.getText().toString().trim();
        final String password =Pass.getText().toString().trim();
        final String name =Name.getText().toString().trim();
        final String gender =School.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Name.setError("Please enter your email");
            Name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Email.setError("Please enter your email");
            Email.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email");
            Email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Pass.setError("Enter a password");
            Pass.requestFocus();
            return;
        }

        progressDialog.setMessage("loding");
        progressDialog.show();
        class RegisterUser extends AsyncTask<Void, Void, String> {



            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);

                //returing the response
                return requestHandler.sendPostRequest(Contants.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
               // progressBar = (ProgressBar) findViewById(R.id.progressBar);
               // progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               progressDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.getBoolean("error")) {

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } else  {
                        Toast.makeText(getApplicationContext(),"This email already exist, please login", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

}