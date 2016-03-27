package com.example.fevrec.uok.tools;

import android.Manifest;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fevrec.uok.R;

import java.util.List;

public class ContactPickerMulti extends ListActivity implements View.OnClickListener {

    //Useless but have to be higher than 0
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int FROM_LIST;

    // List variables
    public String[] Contacts = {};
    public int[] to = {};
    public ListView myListView;

    Button save_button;
    private TextView phone;
    private String[] phoneNumber;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_multi);

        Intent intent = getIntent();
        FROM_LIST = intent.getIntExtra("list",0);

        // Initializing the buttons according to their ID
        save_button = (Button) findViewById(R.id.contact_done);

        // Defines listeners for the buttons
        save_button.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            set_up();
        }

    }

    private void set_up(){
        Cursor mCursor = getContacts();
        startManagingCursor(mCursor);

        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                mCursor,
                Contacts = new String[] { ContactsContract.Contacts.DISPLAY_NAME },
                to = new int[] { android.R.id.text1 });

        setListAdapter(adapter);
        myListView = getListView();
        myListView.setItemsCanFocus(false);
        myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '"
                + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        return managedQuery(uri, projection, selection, selectionArgs,
                sortOrder);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                set_up();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View src) {
        switch (src.getId()) {
            case R.id.contact_done:

                SparseBooleanArray selectedPositions = myListView
                        .getCheckedItemPositions();
                SparseBooleanArray checkedPositions = myListView
                        .getCheckedItemPositions();
                if (checkedPositions != null) {
                    for (int k = 0; k < checkedPositions.size(); k++) {
                        if (checkedPositions.valueAt(k)) {
                            String name =
                                    ((Cursor)myListView.getAdapter().getItem(k)).getString(1);
                            Log.i("XXXX",name + " was selected");

                            long[] id = getListView().getCheckedItemIds();//  i get the checked contact_id instead of position
                            phoneNumber = new String[id.length];
                            for (int j = 0; j < id.length; j++) {

                                phoneNumber[j] = getPhoneNumber(id[j]); // get phonenumber from selected id

                            }

                            Intent pickContactIntent = new Intent();
                            pickContactIntent.putExtra("PICK_CONTACT", phoneNumber);// Add checked phonenumber in intent and finish current activity.
                            pickContactIntent.putExtra("list", FROM_LIST);
                            setResult(RESULT_OK, pickContactIntent);
                            finish();
                        }
                    }
                }

                break;
        }



    }

    private String getPhoneNumber(long id) {
        String phone = null;
        Cursor phonesCursor = null;
        phonesCursor = queryPhoneNumbers(id);
        if (phonesCursor == null || phonesCursor.getCount() == 0) {
            // No valid number
            return null;
        } else if (phonesCursor.getCount() == 1) {
            // only one number, call it.
            phone = phonesCursor.getString(phonesCursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        } else {
            phonesCursor.moveToPosition(-1);
            while (phonesCursor.moveToNext()) {

                // Found super primary, call it.
                phone = phonesCursor.getString(phonesCursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                break;

            }
        }

        return phone;
    }


    private Cursor queryPhoneNumbers(long contactId) {
        ContentResolver cr = getContentResolver();
        Uri baseUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);
        Uri dataUri = Uri.withAppendedPath(baseUri,
                ContactsContract.Contacts.Data.CONTENT_DIRECTORY);

        Cursor c = cr.query(dataUri, new String[] { ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY, ContactsContract.RawContacts.ACCOUNT_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.LABEL }, ContactsContract.Contacts.Data.MIMETYPE + "=?",
                new String[] { ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE }, null);
        if (c != null && c.moveToFirst()) {
            return c;
        }
        return null;
    }
}