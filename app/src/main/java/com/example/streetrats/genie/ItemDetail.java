package com.example.streetrats.genie;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.streetrats.genie.rest.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class ItemDetail extends ActionBarActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        TextView itemName = (TextView) findViewById(R.id.item_name);
        ImageView itemImage = (ImageView) findViewById(R.id.item_image);
        TextView itemDescription = (TextView) findViewById(R.id.item_description);
        TextView itemPrice = (TextView) findViewById(R.id.item_price);
        Button buyButton = (Button) findViewById(R.id.buy_button);

        Product product = (Product) getIntent().getParcelableExtra("PRODUCT");
        String parentActivity = getIntent().getStringExtra("PARENT_ACTIVITY");

        itemName.setText(product.name);
        Picasso.with(getBaseContext())
                .load(product.image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(itemImage);
        itemPrice.setText("Price: $" + String.valueOf(product.price));
        JSONObject features = null;
        StringBuilder result = new StringBuilder();
        try {
            features = new JSONObject(product.features);
            for(int i = 0; i < features.names().length(); i++) {
                result.append(features.names().getString(i) + " : " + features.get(features.names().getString(i)) + '\n');
            }
        } catch (JSONException e) {

        }
        itemDescription.setText(result);

        if(parentActivity.equals("ProfileFragment")) {
            buyButton.setVisibility(View.GONE);
        }
        else {
            buyButton.setVisibility(View.VISIBLE);
            buyButton.setBackgroundColor(Color.WHITE);
        }

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
        }
        return true;
    }
}
