package com.example.streetrats.genie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.streetrats.genie.rest.BuyProductRequest;
import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.Product;
import com.example.streetrats.genie.rest.RestClient;
import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import me.drakeet.materialdialog.MaterialDialog;

public class ProductsFriendAdapter extends RecyclerView.Adapter<ProductsFriendAdapter.ProductFriendViewHolder> {

        private Context context;
        private List<Product> productList;

        private static final int RESULT_OK = 1;
        private static final int RESULT_CANCELED = 0;

        RestClient restClient;
        GenieService genieService;

        public ProductsFriendAdapter(Context context, List<Product> productList) {
            this.productList = productList;
            this.context = context;
            restClient = new RestClient();
            genieService = restClient.getGenieService();
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        @Override
        public void onBindViewHolder(ProductFriendViewHolder productViewHolder, int i) {
            final int position = i;
            final Product p = productList.get(i);

            productViewHolder.vName.setText(p.name);
            Picasso.with(productViewHolder.context)
                    .load(p.image)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(productViewHolder.vImage);

            if(p.bought == true)  {
                productViewHolder.vBought.setVisibility(View.VISIBLE);
            } else {
                productViewHolder.vBought.setVisibility(View.GONE);
            }

            productViewHolder.vDelete.setVisibility(View.GONE);

            productViewHolder.vImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // May return null if a EasyTracker has not yet been initialized with a
                    // property ID.
                    EasyTracker easyTracker = EasyTracker.getInstance(context);

                    // MapBuilder.createEvent().build() returns a Map of event fields and values
                    // that are set and sent with the hit.
                    easyTracker.send(MapBuilder
                                    .createEvent("ui_action",     // Event category (required)
                                            "Product Image Clicked",  // Event action (required)
                                            "list_product_image",   // Event label
                                            null)            // Event value
                                    .build()
                    );

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.item_image, null);
                    ImageView itemImage = (ImageView) view.findViewById(R.id.item_image);
                    Picasso.with(view.getContext())
                            .load(p.image)
                            .placeholder(R.drawable.product)
                            .error(R.drawable.product)
                            .into(itemImage);

                    final me.drakeet.materialdialog.MaterialDialog dialog = new me.drakeet.materialdialog.MaterialDialog(context);
                    dialog.setView(view);
                    dialog.setNegativeButton("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();

                }
            });
            productViewHolder.vName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // May return null if a EasyTracker has not yet been initialized with a
                    // property ID.
                    EasyTracker easyTracker = EasyTracker.getInstance(context);

                    // MapBuilder.createEvent().build() returns a Map of event fields and values
                    // that are set and sent with the hit.
                    easyTracker.send(MapBuilder
                                    .createEvent("ui_action",     // Event category (required)
                                            "Product Info Pressed",  // Event action (required)
                                            "list_product_name",   // Event label
                                            null)            // Event value
                                    .build()
                    );

                    JSONObject features = null;
                    StringBuilder result = new StringBuilder();
                    result.append("Wished By: " + p.owner_first_name + " " + p.owner_last_name + '\n');
                    DecimalFormat df = new DecimalFormat("0.00");
                    String price = df.format(p.price);
                    result.append('\n' + "Price: $" + price + '\n');
                    result.append('\n' + "Item Information:" + '\n');
                    try {
                        features = new JSONObject(p.features);
                        for(int i = 0; i < features.names().length(); i++) {
                            result.append(features.names().getString(i) + " : " + features.get(features.names().getString(i)) + '\n');
                        }
                    } catch (JSONException e) {
                    }



                    final MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                            .title(p.name)
                            .content(result)
                            .positiveText("Grant")
                            .negativeText("Close")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    // May return null if a EasyTracker has not yet been initialized with a
                                    // property ID.
                                    EasyTracker easyTracker = EasyTracker.getInstance(context);

                                    // MapBuilder.createEvent().build() returns a Map of event fields and values
                                    // that are set and sent with the hit.
                                    easyTracker.send(MapBuilder
                                                    .createEvent("ui_action",     // Event category (required)
                                                            "Product Bought",  // Event action (required)
                                                            "Grant",   // Event label
                                                            null)            // Event value
                                                    .build()
                                    );
                                    buyProduct(p._id, position, p.owner_first_name, p.owner);
                                }
                            });
                    dialog.build();
                    dialog.show();
                }
            });
        }

        @Override
        public ProductFriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View productView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.list_products, viewGroup, false);

            ProductFriendViewHolder vh = new ProductFriendViewHolder(productView);

            return vh;
        }

        public static class ProductFriendViewHolder extends RecyclerView.ViewHolder {
            protected View vView;
            protected TextView vName;
            protected ImageView vImage;
            protected ImageView vDelete;
            protected ImageView vBought;
            protected Context context;

            public ProductFriendViewHolder(View v) {
                super(v);
                vView = v;
                vName = (TextView) v.findViewById(R.id.list_product_name);
                vImage = (ImageView) v.findViewById(R.id.list_product_image);
                vDelete = (ImageView) v.findViewById(R.id.product_delete_btn);
                vBought = (ImageView) v.findViewById(R.id.product_bought);
                context = v.getContext();
            }
        }

        public void buyProduct(final String product_id, int _position, String _owner, String _owner_id) {
            if(restClient == null || genieService == null) {
                return;
            }

            final int position = _position;
            final String owner = _owner;
            final String owner_id = _owner_id;

            genieService.buyProduct(new BuyProductRequest(product_id, Session.getActiveSession().getAccessToken().toString()), new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    SnackbarManager.show(
                            Snackbar.with(context) // context
                                    .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                    .text("You Granted " + owner + "'s Wish") // text to be displayed
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                    .animation(false) // don't animate it
                            , (HomeActivity) context); // where it is displayed
                    productList.get(position).bought = true;
                    productList.remove(position);
                    notifyDataSetChanged();
                    /*ParsePush push = new ParsePush();
                    //push.setChannel("channel54fbd7f5e09f1f030039fb75");
                    push.setChannel("channel" + owner_id);
                    push.setMessage("Your Wish Has Been Granted!");
                    push.sendInBackground(new SendCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Log.d("push", "success");
                            }
                            else {
                                Log.d("push", "failure");
                            }
                        }
                    });*/
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    SnackbarManager.show(
                            Snackbar.with(context) // context
                                    .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                    .text("Could Not Buy Item. Something Went Wrong.") // text to be displayed
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                    .animation(false) // don't animate it
                            , (HomeActivity) context); // where it is displayed
                    System.out.println(retrofitError.getMessage());
                }
            });
        }
    }