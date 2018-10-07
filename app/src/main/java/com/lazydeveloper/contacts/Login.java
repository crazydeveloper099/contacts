package com.lazydeveloper.contacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;


public class Login extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText email;
    EditText pass;
    TextView forget;
    Button login;
    Button register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        email=findViewById(R.id.log_email);
        pass=findViewById(R.id.log_pass);
        forget=findViewById(R.id.forget);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);

        //firstly we are checking if user is Already Login From backendless user class
        //this line is true unless the user logs out;
        showProgress(true);
        tvLoad.setText("Checking If You Are In Database");
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response)
            {
                //if user is already login and the backendless return true response

                if(response)
                {



                    //We are extracting user id from class UserIdStorageFactory which stores user id or data of app in internal storage
                    //the true value for keep user logged_in in function Backendless.UserService.login saves an user_id in internal storage
                    //that id will be verified by findbyid if both matched the user will be directed to main activity

                    String id= UserIdStorageFactory.instance().getStorage().get();




                    //if that user id of internal storage is matching with Backendless registered userid then
                    tvLoad.setText("WELCOME BACK YOU ARE ALREADY WITH US");
                    Backendless.Data.of(BackendlessUser.class).findById(id, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response)
                        {
                            background.user=response;
                            //directly go to MainActivity no need to login
                            Login.this.finish();
                            showProgress(false);
                            startActivity(new Intent(Login.this,MainActivity.class));

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error:"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });


                }
                else
                {
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Toast.makeText(Login.this, "ERROR:"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Login.this,Register.class);
                startActivityForResult(intent,1);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "PLEASE ENTER ALL FIELDS", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String a=email.getText().toString();
                    String b=pass.getText().toString();
                    showProgress(true);

                    Backendless.UserService.login(a, b, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response)
                        {
                            background.user=response;
                            showProgress(false);
                            startActivity(new Intent(Login.this,MainActivity.class));
                            Login.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            showProgress(false);
                            Toast.makeText(Login.this, "ERROR:"+(fault.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }, true);
                }
            }

        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e=email.getText().toString();
                if(e.isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter registered email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showProgress(true);
                    Backendless.UserService.restorePassword(e, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {


                            Toast.makeText(Login.this, "password reset instruction sent ton your email", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error:"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this, "USER REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        }




    }









    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
