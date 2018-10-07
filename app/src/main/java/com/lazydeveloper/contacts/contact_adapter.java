package com.lazydeveloper.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivities;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by anubhav on 04-09-2018.
 */

public class contact_adapter extends ArrayAdapter<contacts> implements Filterable {

    static ViewHolder vh;

    private Context context;
    private List<contacts> list_of_contacts;
    public static ImageView edit;
    CustomFilter filter;
    List<contacts> filterList;


    contact_adapter(Context context, List<contacts> objects) {
        super(context, R.layout.list_updated_ui, objects);
        this.context = context;
        this.list_of_contacts = objects;
        this.filterList=objects;




    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert inflater != null;
            convertView = inflater.inflate(R.layout.list_updated_ui, parent, false);

            TextView first = convertView.findViewById(R.id.first_char_list_item);
            TextView name = convertView.findViewById(R.id.name_list_item);
            TextView number = convertView.findViewById(R.id.number_list_item);
            vh = new ViewHolder();
            vh.caller = convertView.findViewById(R.id.edit_list_item);
            vh.caller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num = background.contactsList.get(position).getNumber().trim();
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num)));
                }
            });

            final View finalConvertView = convertView;


            first.setText(list_of_contacts.get(position).getName().toUpperCase().charAt(0) + "");
            char ch=first.getText().charAt(0);
            if(ch>=65 && ch<=68)
            {
                first.setTextColor(Color.parseColor("#FFFFFF"));
                first.setBackgroundColor(Color.parseColor("#311B92"));
            }
            if(ch>=69 && ch<=75)
            {
                first.setTextColor(Color.parseColor("#FFFFFF"));
                first.setBackgroundColor(Color.parseColor("#C62828"));
            }
            if(ch>=76 && ch<=85)
            {
                first.setTextColor(Color.parseColor("#FFFFFF"));
                first.setBackgroundColor(Color.parseColor("#E65100"));
            }
            if(ch>=86 && ch<90)

            {
                first.setTextColor(Color.parseColor("#FFFFFF"));
                first.setBackgroundColor(Color.parseColor("#D500F9"));
            }
            if(ch==90)
            {
                first.setTextColor(Color.parseColor("#FFFFFF"));
                first.setBackgroundColor(Color.parseColor("#263238"));

            }


            name.setText(list_of_contacts.get(position).getName());
            number.setText(list_of_contacts.get(position).getNumber());


            background.pos = position;

            convertView.setTag(vh);
        }
        else
        {
            mainViewHolder=(ViewHolder) convertView.getTag();

        }

            return convertView;
        }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if(filter == null)
        {
            filter=new CustomFilter();
        }

        return filter;
    }
    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();

                ArrayList<contacts> filters=new ArrayList<contacts>();

                //get specific items
                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getName().toUpperCase().contains(constraint))
                    {

                        filters.add(background.contactsList.get(i));
                    }
                }

                results.count=filters.size();
                results.values=filters;

            }else
            {
                results.count=filterList.size();
                results.values=filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub

            list_of_contacts=(List<contacts>) results.values;
            notifyDataSetChanged();
        }

    }

}





