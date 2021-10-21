package com.example.mycontactapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Person.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contact> contacts;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView firstName;
        public TextView lastName;
        public TextView email;
        public TextView number;

        public MyViewHolder(View view) {
            super(view);
            firstName = view.findViewById(R.id.firstname_textview);
            lastName = view.findViewById(R.id.lastname_textview);
            number = view.findViewById(R.id.number_textview);
            email = view.findViewById(R.id.email_textview);

        }

    }

    public ContactAdapter(Context context, ArrayList<Contact> contacts, MainActivity mainActivity) {
        this.context = context;
        this.contacts = contacts;
        this.mainActivity = mainActivity;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Contact contact = contacts.get(position);

        holder.email.setText(contact.getEmail());
        holder.number.setText(contact.getNumber());
        holder.lastName.setText(contact.getLastName());
        holder.firstName.setText(contact.getFirstName());

        holder.itemView.setOnClickListener(v -> mainActivity.addAndEditCars(true, contact, position));
    }

    public int getItemCount() {

        return contacts.size();

    }


}
