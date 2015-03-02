package com.example.streetrats.genie.rest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;

/**
 * Created by saif on 2/28/15.
 */
public class Product implements Parcelable{
    public String _id;
    public String name;
    public String category;
    public double price;
    public String features;
    public String image;
    public String owner;

    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(_id);
        out.writeString(name);
        out.writeString(category);
        out.writeDouble(price);
        out.writeString(features);
        out.writeString(image);
        out.writeString(owner);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            try {
                return new Product(in);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Product(Parcel in) throws JSONException {
        _id = in.readString();
        name = in.readString();
        category = in.readString();
        price = in.readDouble();
        features = in.readString();
        image = in.readString();
        owner = in.readString();
    }

    public void printInfo() {
        System.out.println("Id: " + _id);
        System.out.println("Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Price: " + price);
        System.out.println("Features: " + features);
        System.out.println("Image: " + image);
        System.out.println("Owner " + owner);
    }
}