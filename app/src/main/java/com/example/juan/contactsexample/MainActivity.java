package com.example.juan.contactsexample;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PICK_CONTACT_CODE = 1;

    private GridView contactsGrid;
    //    private Cursor contactsCursor;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        initializeViews();
    }

    private void bindViews() {
        contactsGrid = (GridView) findViewById(R.id.grid);
    }

    public void initializeViews() {
//        contactsCursor = getContentResolver().query(
//                ContactsContract.Contacts.CONTENT_URI,
//                null,
//                null,
//                null,
//                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
//        );
        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
//                contactsCursor,
                null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.TIMES_CONTACTED},
                new int[]{android.R.id.text1, android.R.id.text2},
                0
        );
        contactsGrid.setAdapter(simpleCursorAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        contactsCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.RawContacts.CONTENT_URI);
//            startActivityForResult(intent, PICK_CONTACT_CODE);
            Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, null);
            if (c.moveToFirst()) {
//                do {
//                    Toast.makeText(this, c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)), Toast.LENGTH_SHORT).show();
//                } while (c.moveToNext());
                //Toast.makeText(this, c.getCount() + " DATA", Toast.LENGTH_SHORT).show();
            }
            c.close();

            c = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    //Toast.makeText(this, c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)), Toast.LENGTH_SHORT).show();
                    Cursor data = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            null, "RAW_CONTACT_ID = ?", new String[]{c.getString(c.getColumnIndex(ContactsContract.RawContacts._ID))}, null);
                    if (data.getCount() < 4) {
                        System.out.println("RAW_ID:   " + c.getString(c.getColumnIndex(ContactsContract.RawContacts._ID)));
                        System.out.println("CONTACT_ID:   " + c.getString(c.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)));
                        System.out.println("COUTN DATA:  " + data.getCount());
                        do {
                            try {
                                System.out.println(data.getString(data.getColumnIndex(ContactsContract.Data.DATA1)));
                            } catch (Exception e) {

                            }
                        } while (data.moveToNext());
                    }

                    data.close();
                } while (c.moveToNext());
                //Toast.makeText(this, c.getCount() + " RAW CONTACTS", Toast.LENGTH_SHORT).show();
            }
            c.close();

            c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (c.moveToFirst()) {
//                do {
//                    Toast.makeText(this, c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)), Toast.LENGTH_SHORT).show();
//                } while (c.moveToNext());
                //Toast.makeText(this, c.getCount() + " CONTACTS", Toast.LENGTH_SHORT).show();
            }
            c.close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode == PICK_CONTACT_CODE && resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            }
            c.close();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.TIMES_CONTACTED},
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}
