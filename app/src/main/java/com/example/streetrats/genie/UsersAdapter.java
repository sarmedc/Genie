package com.example.streetrats.genie;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {
    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_users_layout, parent, false);
        }

        // Lookup view for data population
        ImageView user_image = (ImageView) convertView.findViewById(R.id.list_user_image);
        TextView user_name = (TextView) convertView.findViewById(R.id.list_user_name);

        // Populate the data into the template view using the data object
        //user_image.setImageResource(R.drawable.product);
        user_name.setText(user.first_name + " " + user.last_name);
        /*new DownloadImageTask((ImageView) convertView.findViewById(R.id.list_user_image))
                .execute(user.image);*/
        Picasso.with(getContext())
                .load(user.image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(user_image);


        // Return the completed view to render on screen
        return convertView;
     }

    Drawable drawable_from_url(String url, String src_name) throws
            java.net.MalformedURLException, java.io.IOException
    {
        return Drawable.createFromStream(((java.io.InputStream)
                new java.net.URL(url).getContent()), src_name);
    }
 }