package com.lazydeveloper.contacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class info extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView first;
    TextView namea;
    ImageView call;
    ImageView message;
    ImageView edit;
    ImageView delete;
    EditText name;
    EditText email;
    EditText phone;
    Button submit1;
    List<contacts> b;
    static int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        first = findViewById(R.id.firstchar);
        namea = findViewById(R.id.name);
        call = findViewById(R.id.phone);
        message = findViewById(R.id.message);
        edit = findViewById(R.id.edit00);
        delete = findViewById(R.id.delete);
        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_number);
        submit1=findViewById(R.id.submit);

        b = background.contactsList;
        pos = getIntent().getIntExtra("index", 0);

        String first_char = b.get(pos).getName().charAt(0) + "";
        first.setText(first_char);

        namea.setText(b.get(pos).getName());

        name.setText(b.get(pos).getName());
        email.setText(b.get(pos).getEmail());
        phone.setText(b.get(pos).getNumber());

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = b.get(pos).getNumber();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                startActivity(intent);


            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = b.get(pos).getNumber();

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + num));
                startActivity(intent);


            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = b.get(pos).getNumber();

                Intent intent = new Intent(info.this, edit_me.class);
                startActivity(intent);


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String n=background.contactsList.get(pos).getName();
                AlertDialog.Builder dialog=new AlertDialog.Builder(info.this);
                dialog.setMessage("Delete "+n+" from contacts list ?");
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        showProgress(true);
                        tvLoad.setText("Deleting...");

                        Backendless.Persistence.of(contacts.class).remove(b.get(pos), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response)
                            {
                                showProgress(false);
                                background.contactsList.remove(pos);
                                setResult(RESULT_FIRST_USER,new Intent());
                                info.this.finish();

                            }

                            @Override
                            public void handleFault(BackendlessFault fault)
                            {
                                Toast.makeText(info.this, "Error :"+fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });



                    }
                });
                dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();


            }

        });
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String nm=name.getText().toString().trim();
                String em=email.getText().toString().trim();
                String ph=phone.getText().toString().trim();

                if(nm.isEmpty() || em.isEmpty()|| ph.isEmpty())
                {
                    Toast.makeText(info.this, "Please enter all the fileds!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showProgress(true);
                    tvLoad.setText("please wait...while saving...");
                    background.contactsList.get(pos).setName(nm);
                    background.contactsList.get(pos).setEmail(em);
                    background.contactsList.get(pos).setNumber(ph);
                    Backendless.Persistence.save(background.contactsList.get(pos), new AsyncCallback<contacts>() {
                        @Override
                        public void handleResponse(contacts response)
                        {
                            showProgress(false);

                            setResult(5);
                            info.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            Toast.makeText(info.this, "Error :"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            setResult(RESULT_OK,new Intent());
            info.this.finish();

        }






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
