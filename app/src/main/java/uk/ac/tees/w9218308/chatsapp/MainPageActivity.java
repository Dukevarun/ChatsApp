package uk.ac.tees.w9218308.chatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import uk.ac.tees.w9218308.chatsapp.Fragments.CameraFragment;
import uk.ac.tees.w9218308.chatsapp.Fragments.ChatListFragment;
import uk.ac.tees.w9218308.chatsapp.Utils.SectionsPagerAdapter;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainPageActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private FloatingActionButton mFindUser;
    private ViewPager mViewPager;
    private TabLayout mTabs;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();

        Fresco.initialize(this);

        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

        mToolBar = findViewById(R.id.mainToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("ChatsApp");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mSectionsPagerAdapter.addFragment(new CameraFragment(), "Camera");
        mSectionsPagerAdapter.addFragment(new ChatListFragment(), "Chats");
        mViewPager = findViewById(R.id.tabPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabs = findViewById(R.id.tabs);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.getTabAt(1).select();


        mFindUser = findViewById(R.id.findUsers);
        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
            }
        });

        getPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.option_editProfile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;

            case R.id.option_logout:
                OneSignal.setSubscription(false);
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;

            default:
                return false;
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},2);
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 3);
        }
    }
}
