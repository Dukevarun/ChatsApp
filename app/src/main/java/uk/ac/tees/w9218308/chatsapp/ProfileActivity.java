package uk.ac.tees.w9218308.chatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.w9218308.chatsapp.User.UserObject;

public class ProfileActivity extends AppCompatActivity {

    Toolbar pToolBar;
    EditText pName, pStatus;
    CircleImageView pImage;
    Button pUpdate;

    FirebaseAuth mAuth;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeFields();


        pUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        retrieveUserInfo();
    }


    private void updateUserInfo() {

        String profileName = "", profileStatus = "";

        if (!pName.getText().toString().isEmpty())
            profileName = pName.getText().toString();
        if (!pStatus.getText().toString().isEmpty())
            profileStatus = pStatus.getText().toString();

        HashMap profileMap = new HashMap();
        profileMap.put("name", profileName);
        profileMap.put("status", profileStatus);

        DatabaseReference userProfileDB = FirebaseDatabase.getInstance().getReference("user").child(currentUserId);
        userProfileDB.updateChildren(profileMap);

        Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    private void retrieveUserInfo() {

        String name = "", status = "", image = "";
        DatabaseReference userProfileDB = FirebaseDatabase.getInstance().getReference("user").child(currentUserId);
        userProfileDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
//                    String image = dataSnapshot.child("image").getValue().toString();

                    pName.setText(name);
                    pStatus.setText(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeFields() {

        mAuth = FirebaseAuth.getInstance();
        pToolBar = findViewById(R.id.profileBar);
        pName = findViewById(R.id.profileName);
        pStatus = findViewById(R.id.profileStatus);
        pImage = findViewById(R.id.profile_image);
        pUpdate = findViewById(R.id.update);

        setSupportActionBar(pToolBar);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            }
        });

        currentUserId = mAuth.getCurrentUser().getUid();
    }
}
