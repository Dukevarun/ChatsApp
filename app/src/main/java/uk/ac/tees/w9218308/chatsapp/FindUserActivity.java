package uk.ac.tees.w9218308.chatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.tees.w9218308.chatsapp.User.UserListAdapter;
import uk.ac.tees.w9218308.chatsapp.User.UserObject;
import uk.ac.tees.w9218308.chatsapp.Utils.CountryToPhonePrefix;

public class FindUserActivity extends AppCompatActivity{

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> contactList, userList;

//    Toolbar mToolbar;

//    public boolean is_in_action_mode = false;
//    TextView counterTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList = new ArrayList<>();
        userList = new ArrayList<>();

        /*mToolbar = findViewById(R.id.recyclerViewBar);
        setSupportActionBar(mToolbar);

        counterTextView = findViewById(R.id.counterText);
        counterTextView.setVisibility(View.GONE);*/

        Button mCreate = findViewById(R.id.create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
            }
        });

        /*Toolbar mToolBar = findViewById(R.id.findUserToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Select Contact");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        /*mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            }
        });*/

        initializeRecyclerView();
        getContactList();
    }

    public void createChat() {
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference chatInfoDB = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("user");

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);

        boolean validChat = false;
        for (UserObject mUser : userList) {
            if (mUser.getSelected()) {
                validChat = true;
                newChatMap.put("users/" + mUser.getUid(), true);
                userDB.child(mUser.getUid()).child("chat").child(key).setValue(true);
            }
        }

        if (validChat) {
            chatInfoDB.updateChildren(newChatMap);
            userDB.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        }
    }

    private void getContactList() {

        String ISOPrefix = getCountryISO();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String status = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STATUS));
            /*String image = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            Bitmap bp = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.profile_image);

            if (image != null) {
                try {
                    bp = MediaStore.Images.Media
                            .getBitmap(getApplicationContext().getContentResolver(),
                                    Uri.parse(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if (!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOPrefix + phone;

            UserObject mContact = new UserObject("", name, phone, status/*, bp*/);
            contactList.add(mContact);
            getUserDetails(mContact);
        }
    }

    private void getUserDetails(UserObject mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String phone = "", name = "", status = "";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if (childSnapshot.child("phone").getValue() != null)
                            phone = childSnapshot.child("phone").getValue().toString();
                        if (childSnapshot.child("name").getValue() != null)
                            name = childSnapshot.child("name").getValue().toString();
                        if (childSnapshot.child("status").getValue() != null)
                            status = childSnapshot.child("status").getValue().toString();
                        /*if (childSnapshot.child("image").getValue() != null)
                            phone = childSnapshot.child("image").getValue().toString();*/

                        UserObject mUser = new UserObject(childSnapshot.getKey(), name, phone, status/*, image*/);

                        if (name.equals(phone)) {
                            for (UserObject mContactIterator : contactList) {
                                if (mContactIterator.getPhone().equals(mUser.getPhone())) {
                                    mUser.setName(mContactIterator.getName());
                                    mUser.setStatus(mContactIterator.getStatus());
                                }
                            }
                        }

                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.);
        return true;
    }*/

    private String getCountryISO() {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null) {
            if (!telephonyManager.getNetworkCountryIso().toString().equals("")) {
                iso = telephonyManager.getNetworkCountryIso().toString();
            }
        }

        return CountryToPhonePrefix.getPhone(iso);
    }

    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserListAdapter(getApplicationContext(),userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}