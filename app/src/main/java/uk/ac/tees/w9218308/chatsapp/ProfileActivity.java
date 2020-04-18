package uk.ac.tees.w9218308.chatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private EditText mUserName, mUserStatus;
    private Button mUpdate;
    private CircleImageView mProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialiseFields();

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        retrieveUserInfo();
    }

    private void updateProfile() {

        final String userName = mUserName.getText().toString();
        final String pStatus = mUserStatus.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please enter User Name", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child("user").child(currentUser.getUid()).child("name").setValue(userName);
            dbRef.child("user").child(currentUser.getUid()).child("status").setValue(pStatus);
        }
        sendUserToMainActivity();
    }

    private void retrieveUserInfo() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("user").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))) {
                    String uName = dataSnapshot.child("name").getValue().toString();
                    String uStatus = dataSnapshot.child("status").getValue().toString();
                    String uImage = dataSnapshot.child("image").getValue().toString();

                    mUserName.setText(uName);
                    mUserStatus.setText(uStatus);

                } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                    String uName = dataSnapshot.child("name").getValue().toString();
                    String uStatus = dataSnapshot.child("status").getValue().toString();

                    mUserName.setText(uName);
                    mUserStatus.setText(uStatus);
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void initialiseFields() {
        mUserName = findViewById(R.id.userName);
        mUserStatus = findViewById(R.id.status);
        mUpdate = findViewById(R.id.updateProfile);
        mProfilePicture = findViewById(R.id.profileImage);
    }
}
