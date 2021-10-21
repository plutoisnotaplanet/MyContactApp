package com.example.mycontactapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mycontactapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Data.ContactAppDatabase;
import Person.Contact;

public class MainActivity extends AppCompatActivity {

    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAppDatabase contactAppDatabase;

    private ActivityMainBinding activityMainBinding;
    private MainButtonHandler buttonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        contactAppDatabase = Room.databaseBuilder(getApplicationContext(),
                ContactAppDatabase.class, "ContactsDB")
                .build();

        new GetAllContactsAsyncTask().execute();

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        buttonHandler = new MainButtonHandler(this);
        activityMainBinding.setButtonHandler(buttonHandler);


        contactAdapter = new ContactAdapter(this, contactArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

//        FloatingActionButton floatingActionButton =
//                (FloatingActionButton) findViewById(R.id.floating_action_button);
//
//        floatingActionButton.setOnClickListener(v -> addAndEditCars(false,null,-1));
    }

        public void addAndEditCars(final boolean isUpdate, final Contact contact, final int position) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.layout_add_contact, null);

            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilderUserInput.setView(view);

            TextView newContactTitle = view.findViewById(R.id.newContactTitle);
            final EditText firstNameEdit = view.findViewById(R.id.edit_text_firstName);
            final EditText lastNameEdit = view.findViewById(R.id.edit_text_lastName);
            final EditText numberEdit = view.findViewById(R.id.edit_text_phone);
            final EditText emailEdit = view.findViewById(R.id.edit_text_email);

            newContactTitle.setText(!isUpdate ? "Add Contact" : "Edit Contact");

            if (isUpdate && contact != null) {
                firstNameEdit.setText(contact.getFirstName());
                lastNameEdit.setText(contact.getLastName());
                numberEdit.setText(contact.getNumber());
                emailEdit.setText(contact.getEmail());
            }

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(isUpdate ? "Delete" : "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (isUpdate) {

                                        deleteContact(contact, position);

                                    }
                                    else {

                                        dialog.cancel();

                                    }
                                }
                            });

            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

                if (TextUtils.isEmpty(firstNameEdit.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter first name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(lastNameEdit.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter last name!", Toast.LENGTH_SHORT).show();
                    return;
            }
                else if (TextUtils.isEmpty(emailEdit.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter e-mail!", Toast.LENGTH_SHORT).show();
                    return;
        }
                else if (TextUtils.isEmpty(numberEdit.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter phone number!", Toast.LENGTH_SHORT).show();
                    return; }
                else {
                    alertDialog.dismiss();
                }

                if (isUpdate && contact != null) {

                    updateContact(firstNameEdit.getText().toString(), lastNameEdit.getText().toString(),
                            emailEdit.getText().toString(), numberEdit.getText().toString(), position);

                }
                else {

                    createContact(firstNameEdit.getText().toString(), lastNameEdit.getText().toString(),
                            emailEdit.getText().toString(), numberEdit.getText().toString());

                }


    });
}

    private void updateContact(String name, String lastname, String email, String number, int position) {

        Contact contact = contactArrayList.get(position);

        contact.setEmail(email);
        contact.setFirstName(name);
        contact.setLastName(lastname);
        contact.setNumber(number);

        new UpdateContactAsyncTask().execute(contact);

        contactArrayList.set(position,contact);

        }

    private void deleteContact(Contact contact, int position) {

            contactArrayList.remove(position);

            new DeleteContactAsyncTask().execute(contact);

        }

    private void createContact(String firstName, String lastName, String email, String number) {

            new CreateContactAsyncTask().execute(new Contact(0,firstName,lastName,email,number));

        }

    private class CreateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {

            long id = contactAppDatabase.getContactDao().addContact(
                    contacts[0]
            );

            Contact contact = contactAppDatabase.getContactDao().getContact(id);

            if (contact != null) {

                contactArrayList.add(0, contact);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            contactAdapter.notifyDataSetChanged();

        }
    }

    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {

            contactAppDatabase.getContactDao().updateContact(contacts[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            contactAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {

        @Override
        protected Void doInBackground(Contact... contacts) {

            contactAppDatabase.getContactDao().deleteContact(contacts[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            contactAdapter.notifyDataSetChanged();
        }
    }

    private class GetAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            contactAppDatabase.getContactDao().getAllContacts();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            contactAdapter.notifyDataSetChanged();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            contactArrayList.remove(viewHolder.getAdapterPosition());
            contactAdapter.notifyDataSetChanged();
        }
    };
    public class MainButtonHandler {

        Context context;

        public MainButtonHandler(Context context) {
            this.context = context;
        }

        public void buttonHandlerOnClick (View view) {
            addAndEditCars(false,null,-1);
        }
    }


}