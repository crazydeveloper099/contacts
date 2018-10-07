package com.lazydeveloper.contacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    DataQueryBuilder dqb=DataQueryBuilder.create();
    String whereClause;


    contact_adapter ca;
    ListView lv;
    TextView tv;

    TextView mEmptyView;



    SearchView sv;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         sv=findViewById(R.id.search);

        mEmptyView = (TextView) findViewById(R.id.emptyView);




        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lv=findViewById(R.id.list_vie);
        tv=findViewById(R.id.add_new);


        whereClause="userEmail='"+background.user.getEmail()+"'";


        dqb.setWhereClause(whereClause);
        dqb.setGroupBy("name");


       showProgress(true);
        tvLoad.setText("GETTING YOUR CONTACTS BUDDY.....");

        Backendless.Persistence.of(contacts.class).find(dqb, new AsyncCallback<List<contacts>>() {
            @Override
            public void handleResponse(List<contacts> response)
            {
                background.contactsList=response;
                ca=new contact_adapter(MainActivity.this,response);
                lv.setAdapter(ca);
                showProgress(false);



            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                showProgress(false);
                Toast.makeText(MainActivity.this, "ERROR :"+fault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(MainActivity.this,addcontact.class),1);

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(MainActivity.this,info.class);
                intent.putExtra("index",i);
                startActivityForResult(intent,2);

            }
        });
        lv.setTextFilterEnabled(true);
        lv.setEmptyView( mEmptyView);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // TODO Auto-generated method stub

                ca.getFilter().filter(query);

                return false;
            }
        });









    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1) {
            if (resultCode == RESULT_OK) {

                showProgress(true);
                tvLoad.setText("Upadting contact list....");
                Backendless.Persistence.of(contacts.class).find(dqb, new AsyncCallback<List<contacts>>() {
                    @Override
                    public void handleResponse(List<contacts> response)
                    {
                        background.contactsList=response;

                        ca=new contact_adapter(MainActivity.this,response);
                        lv.setAdapter(ca);
                        showProgress(false);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, "Error :"+fault.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                Toast.makeText(this, "BINGO!!! ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this, "HURRAY DONE !!!", Toast.LENGTH_SHORT).show();
                showProgress(true);
                tvLoad.setText("Updating Contacts.....");

                Backendless.Persistence.of(contacts.class).find(dqb, new AsyncCallback<List<contacts>>() {
                    @Override
                    public void handleResponse(List<contacts> response)
                    {
                        background.contactsList=response;
                        showProgress(false);
                        ca=new contact_adapter(MainActivity.this,response);
                        lv.setAdapter(ca);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, "ERROR :"+fault.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
            if(resultCode==RESULT_FIRST_USER)
            {
                ca=new contact_adapter(MainActivity.this,background.contactsList);
                lv.setAdapter(ca);


            }
            if(resultCode==5)
            {
                ca=new contact_adapter(MainActivity.this,background.contactsList);
                lv.setAdapter(ca);
            }


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




