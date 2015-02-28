package com.example.streetrats.genie;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.streetrats.genie.rest.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<Product> {
    public ProductsAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_products_layout, parent, false);
        }

        // Lookup view for data population
        ImageView product_image = (ImageView) convertView.findViewById(R.id.list_product_image);
        TextView product_name = (TextView) convertView.findViewById(R.id.list_product_name);

        // Populate the data into the template view using the data object
        //user_image.setImageResource(R.drawable.product);
        product_name.setText(product.name);
        /*new DownloadImageTask((ImageView) convertView.findViewById(R.id.list_user_image))
                .execute(user.image);*/
        Picasso.with(getContext())
                .load(product.image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(product_image);


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