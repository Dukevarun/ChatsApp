package uk.ac.tees.w9218308.chatsapp.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.w9218308.chatsapp.ChatActivity;
import uk.ac.tees.w9218308.chatsapp.FindUserActivity;
import uk.ac.tees.w9218308.chatsapp.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;
    Context context;

    public UserListAdapter(Context context, ArrayList<UserObject> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, final int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mStatus.setText(userList.get(position).getStatus());
        if (userList.get(position).getImageUrl().equals("default"))
            holder.mImage.setImageResource(R.drawable.profile_image);
        else
            Glide.with(context).load(userList.get(position).getImageUrl()).into(holder.mImage);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
            /*Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userObject", userList.get(position));
            context.startActivity(intent);*/
        }
    }


    class UserListViewHolder extends RecyclerView.ViewHolder {

        TextView mName, mStatus;
        CircleImageView mImage;
        LinearLayout mLayout;

        UserListViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mStatus = view.findViewById(R.id.status);
            mImage = view.findViewById(R.id.image);
            mLayout = view.findViewById(R.id.userLayout);
        }
    }
}