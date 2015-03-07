package com.example.streetrats.genie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.streetrats.genie.rest.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> userList;

    public UsersAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        User u = userList.get(i);
        userViewHolder.vName.setText(u.first_name + " " + u.last_name);
        Picasso.with(userViewHolder.context)
                .load(u.image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(userViewHolder.vImage);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View userView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_users, viewGroup, false);

        return new UserViewHolder(userView);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected ImageView vImage;
        protected Context context;

        public UserViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.list_user_name);
            vImage = (ImageView)  v.findViewById(R.id.list_user_image);
            context = v.getContext();
        }
    }
}