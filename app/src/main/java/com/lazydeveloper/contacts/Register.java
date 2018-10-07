package com.lazydeveloper.contacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Objects;
//TO REGISTER THE USER
public class Register extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText name;
    EditText email;
    EditText phone;
    EditText pass;
    EditText confirm_pass;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);





        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        pass=findViewById(R.id.password);
        confirm_pass=findViewById(R.id.confirm_password);
        reg=findViewById(R.id.reg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nam = name.getText().toString();
                String emai = email.getText().toString();
                String pho = phone.getText().toString();
                String pas = pass.getText().toString();
                String conf = confirm_pass.getText().toString();

                //TO CHECK IF ANY FIELD IS BLANK IF BLANK THEN POP UP A TOAST

                if (nam.isEmpty() || emai.isEmpty() || pho.isEmpty() || pas.isEmpty()) {
                    Toast.makeText(Register.this, "PLEASE ENTER ALL DETAILS CORRECTLY", Toast.LENGTH_SHORT).show();
                }

                //IF NOT BLANK THEN PROCEED

                else
                {
                    //CHECKING IF PASSWORD AND CONFIRM PASSWORD IS EQUAL

                    if (pas.equals(conf))
                    {
                        //BACKENDLESSUSER IS A FUNCTION TO REGISTER NEW USER IN BACKENDLESS USER TAB.

                        //setProperty IS FOR ANYTHING IN BACKENDLESS USER TAB THAT IS NOT DEFAULT COLUMN

                        //FOR EXAMPLE NAME IS CREATED AND NUMBER IS CREATED BY ME IN BACKENDLESS USER

                        //IN  setProperty WE HAVE TO GIVE KEY VALUE PAIR EXAMPLE ""name"" IS KEY AND VALUE IS nam REMEMBER ""name"".

                        //SAME FOR ""number""

                        BackendlessUser bu=new BackendlessUser();
                        bu.setEmail(emai);

                        bu.setPassword(pas);
                        bu.setProperty("name",nam);
                        bu.setProperty("number",pho);

                        //showProgress WILL SHOW THAT ROTATING CIRCLE

                        showProgress(true);

                        //HERE WE ARE CALLING Backendless-> UserService( IN WEBSITE)->REGISTER NEW USER BY USING register

                        Backendless.UserService.register(bu, new AsyncCallback<BackendlessUser>() {
                            @Override

                            //handleResponse if work is done and user is registered successfully

                            public void handleResponse(BackendlessUser response)
                            {

                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                Register.this.finish();
                            }

                            //handleFault WILL HANDLE IF THERE IS ANY ERROR REMEMBER!!! THAT fault IS A MESSAGE GIVEN BY BACKENDLESS
                            //AND YOU CAN PRINT THIS.


                            @Override
                            public void handleFault(BackendlessFault fault)
                            {
                                showProgress(false);
                                Toast.makeText(Register.this, "ERROR:"+fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        Register.this.finish();
                    }
                    else
                    {
                        Toast.makeText(Register.this, "PASSWORD AND CONFIRM PASSWORD DOES NOT MATCH", Toast.LENGTH_SHORT).show();
                    }
                }

                }










        });


    }












    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

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

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

