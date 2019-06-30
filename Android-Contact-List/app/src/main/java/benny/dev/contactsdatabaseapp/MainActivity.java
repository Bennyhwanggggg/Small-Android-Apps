package benny.dev.contactsdatabaseapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

// Demo do see how to use content provider
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView contactNames;
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
//    private static boolean READ_CONTACTS_GRANTED = false; // hard to keep track of this state if stored in a variable so should avoid this.
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactNames = (ListView) findViewById(R.id.contact_names);

        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        Log.d(TAG, "onCreate: checkSelfPermission -> " + hasReadContactPermission);

//        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "onCreate: Permission granted");
////            READ_CONTACTS_GRANTED = true;
//        } else {
//            Log.d(TAG, "onCreate: Permission denied");
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
//        }
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onCreate: Requesting permissions");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        // to find out whether permission is granted or denied, we need a call back method that is called once the user has made a decision.

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starts");
//                if (READ_CONTACTS_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){ // use this to check permission instead of storing permission in a variable
                    String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                    ContentResolver contentResolver = getContentResolver();
                    // content resolver returns a cursor
                    // projection is just a string array of the names we want to query
                    // think of the third as the where function in sql.
                    // fourth is to select a specific column e.g name=Bob
                    // Last is the order
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
                    if (cursor != null) {
                        List<String> contacts = new ArrayList<String>();
                        while (cursor.moveToNext()) {
                            contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                        }
                        cursor.close();
                        // we have to pass MainActivity.this because if we just pass this, it is referring to the onClickListener.
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.contact_detail, R.id.name, contacts);
                        contactNames.setAdapter(adapter);
                    }
                } else {
                    // Snackbar is limited to two lines of text.
                    Snackbar.make(view, "This app cannot display your contacts record...", Snackbar.LENGTH_INDEFINITE).setAction("Grant Access",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "onClick: SnackBar clicked");
                                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)){
                                        Log.d(TAG, "onClick: Snackbar shouldShowRequestPermission start");
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
                                    } else {
                                        // users permanently denied so take them to setting
                                        Log.d(TAG, "onClick: launch setting");
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                                        Log.d(TAG, "onClick: Intent uri is " + uri.toString());
                                        intent.setData(uri);
                                        MainActivity.this.startActivity(intent);
                                    }
                                    Log.d(TAG, "onClick: SnackBar onclick end");
                                }
                            }).show();
                }
                Log.d(TAG, "onClick: ends");
            }
        });
        Log.d(TAG, "onCreate: ends");
    }

    // Often we are not interested in whether permission is granted or not as we write code that deals with both case anyway.
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult: starts");
//        switch (requestCode) {
//            case REQUEST_CODE_READ_CONTACTS:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
////                    READ_CONTACTS_GRANTED = true;
//                } else {
//                    Log.d(TAG, "onRequestPermissionsResult: Permission denied");
//                    // disable functions that required permission here...
//                    // e.g disable the floating action button
//                }
////                fab.setEnabled(READ_CONTACTS_GRANTED);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
