package com.example.streetrats.genie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.streetrats.genie.rest.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

        private Context context;
        private List<Product> productList;

        public ProductsAdapter(Context context, List<Product> productList) {
            this.productList = productList;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        @Override
        public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
            final int position = i;
            Product p = productList.get(i);
            productViewHolder.vName.setText(p.name);
            Picasso.with(productViewHolder.context)
                    .load(p.image)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(productViewHolder.vImage);
            productViewHolder.vView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra("PRODUCT", productList.get(position)).putExtra("PARENT_ACTIVITY", "HomeFragment");
                    context.startActivity(intent);
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
            protected Context context;

            public ProductViewHolder(View v) {
                super(v);
                vView = v;
                vName =  (TextView) v.findViewById(R.id.list_product_name);
                vImage = (ImageView)  v.findViewById(R.id.list_product_image);
                context = v.getContext();
            }
        }
    }