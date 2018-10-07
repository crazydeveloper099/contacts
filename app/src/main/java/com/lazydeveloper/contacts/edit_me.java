package com.lazydeveloper.contacts;

import android.support.v7.app.AppCompatActivity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;


public class edit_me extends AppCompatActivity {
    EditText name;
    EditText number;
    EditText email;
    Button save;
    Button cancel;
    List<contacts> a;
    int p;


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_me);

        name = findViewById(R.id.edit_name);
        number = findViewById(R.id.edit_number);
        email = findViewById(R.id.edit_email);
        save = findViewById(R.id.save_it);
        cancel = findViewById(R.id.cancel);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        a=background.contactsList;


        p=getIntent().getIntExtra("index",0);

        String nam=a.get(p).getName();
        String numbe=a.get(p).getNumber();
        String emai=a.get(p).getEmail();
        name.setText(nam);
        number.setText(numbe);
        email.setText(emai);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showProgress(true);
                tvLoad.setText("HEY HANG ON WHILE GIVING YOUR FRIEND A ROOM....");
                String nme=name.getText().toString();
                String num=number.getText().toString();
                String ema=email.getText().toString();

                a.get(p).setName(nme);
                a.get(p).setNumber(num);
                a.get(p).setEmail(ema);

                Backendless.Persistence.save(a.get(p), new AsyncCallback<contacts>() {
                    @Override
                    public void handleResponse(contacts response)
                    {
                        showProgress(false);
                        Toast.makeText(edit_me.this, "HURRAY DONE !!!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK,new Intent());
                        edit_me.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        showProgress(false);
                        Toast.makeText(edit_me.this, "ERROR :"+fault.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED,new Intent());
                edit_me.this.finish();
            }
        });

    }
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        private void showProgress ( final boolean show){
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
