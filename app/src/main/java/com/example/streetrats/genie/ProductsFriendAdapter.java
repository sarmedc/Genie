package com.example.streetrats.genie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.Product;
import com.example.streetrats.genie.rest.RestClient;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import me.drakeet.materialdialog.MaterialDialog;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

        private Context context;
        private List<Product> productList;

        private static final int RESULT_OK = 1;
        private static final int RESULT_CANCELED = 0;

        RestClient restClient;
        GenieService genieService;

        public ProductsAdapter(Context context, List<Product> productList) {
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
        public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
            final int position = i;
            final Product p = productList.get(i);

            productViewHolder.vName.setText(p.name);
            Picasso.with(productViewHolder.context)
                    .load(p.image)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(productViewHolder.vImage);

            productViewHolder.vDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeProduct(p._id, position);
                }
            });
            productViewHolder.vImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra("PRODUCT", productList.get(position)).putExtra("PARENT_ACTIVITY", "HomeFragment");
                    context.startActivity(intent);*/
                    /*new MaterialDialog.Builder(context)
                            .title("Title")
                            .content("Content")
                            .positiveText("Agree")
                            .negativeText("Disagree")
                            .show();*/

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
                    /*Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra("PRODUCT", productList.get(position)).putExtra("PARENT_ACTIVITY", "HomeFragment");
                    context.startActivity(intent);*/
                    JSONObject features = null;
                    StringBuilder result = new StringBuilder("Features:" + '\n');
                    try {
                        features = new JSONObject(p.features);
                        for(int i = 0; i < features.names().length(); i++) {
                            result.append(features.names().getString(i) + " : " + features.get(features.names().getString(i)) + '\n');
                        }
                    } catch (JSONException e) {
                    }
                    result.append('\n' + "Price: $" + p.price);


                    /*final MaterialDialog dialog = new MaterialDialog(context);
                    dialog.setTitle(p.name);
                    dialog.setMessage(result);
                    dialog.setPositiveButton("Buy", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SnackbarManager.show(
                                    Snackbar.with(context) // context
                                            .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                            .text("You Bought This Item") // text to be displayed
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                            .animation(false) // don't animate it
                                    , (HomeActivity)context); // where it is displayed
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();*/
                    final MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                            .title(p.name)
                            .content(result)
                            .positiveText("Buy")
                            .negativeText("Close")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    SnackbarManager.show(
                                            Snackbar.with(context) // context
                                                    .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                                    .text("You Bought This Item") // text to be displayed
                                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                                    .animation(false) // don't animate it
                                            , (HomeActivity)context); // where it is displayed
                                }
                            });
                    dialog.build();
                    dialog.show();
                }
            });
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View productView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.list_products, viewGroup, false);

            ProductViewHolder vh = new ProductViewHolder(productView);

            return vh;
        }

        public static class ProductViewHolder extends RecyclerView.ViewHolder {
            protected View vView;
            protected TextView vName;
            protected ImageView vImage;
            protected ImageView vDelete;
            protected Context context;

            public ProductViewHolder(View v) {
                super(v);
                vView = v;
                vName =  (TextView) v.findViewById(R.id.list_product_name);
                vImage = (ImageView)  v.findViewById(R.id.list_product_image);
                vDelete = (ImageView) v.findViewById(R.id.product_delete_btn);
                context = v.getContext();
            }
        }

        public void removeProduct(String _id, int _position) {
            final String id = _id;
            final int position = _position;

            if(restClient == null || genieService == null) {
                return;
            }
            genieService.removeProduct(id, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    SnackbarManager.show(
                            Snackbar.with(context) // context
                                    .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                    .text("You Removed An Item") // text to be displayed
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                    .animation(false) // don't animate it
                            , (HomeActivity)context); // where it is displayed
                    productList.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    SnackbarManager.show(
                            Snackbar.with(context) // context
                                    .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                    .text("Could Not Remove Item. Something Went Wrong.") // text to be displayed
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                    .animation(false) // don't animate it
                            , (HomeActivity)context); // where it is displayed
                    System.out.println(retrofitError.getMessage());
                }
            });
        }
    }