package uk.ac.tees.w9218308.chatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.w9218308.chatsapp.User.UserObject;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_INTENT = 100;

    private Toolbar pToolBar;
    private EditText pName, pStatus;
    private CircleImageView pImage;
    private Button pUpdate;

    private FirebaseAuth mAuth;

    private StorageReference storageReference;
    private Uri imageUri;
    private StorageTask uploadTask;


    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeFields();

        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


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
                String name, status, image;
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("name").getValue().toString();
                    status = dataSnapshot.child("status").getValue().toString();
                    image = dataSnapshot.child("image").getValue().toString();

                    if (!name.equals("default"))
                        pName.setText(name);
                    if (!status.equals("default"))
                        pStatus.setText(status);
                    if (dataSnapshot.child("image").getValue().equals("default"))
                        pImage.setImageResource(R.drawable.profile_image);
                    else
                        Glide.with(getApplicationContext()).load(dataSnapshot.child("image").getValue()).into(pImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_INTENT && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(currentUserId);
                        HashMap<String, Object> imageMap = new HashMap<>();
                        imageMap.put("image", mUri);
                        reference.updateChildren(imageMap);

                        pd.dismiss();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image Selected", Toast.LENGTH_SHORT).show();
        }
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
        storageReference = FirebaseStorage.getInstance().getReference().child("profile image");
    }
}
