package uk.ac.tees.w9218308.chatsapp.User;

import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

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

        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());
        holder.mStatus.setText(userList.get(position).getStatus());
        holder.mImage.setImageBitmap(userList.get(position).getImage());

        /*if (!findUserActivity.is_in_action_mode) {
            holder.mAdd.setVisibility(View.GONE);
        } else {
            holder.mAdd.setVisibility(View.VISIBLE);
            holder.mAdd.setChecked(false);
        }

        holder.mLayout.setOnLongClickListener();*/

        holder.mAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userList.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    /*public void createChat(int position) {
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);
        newChatMap.put("users/" + userList.get(position).getUid(), true);

        DatabaseReference chatInfoDB = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        chatInfoDB.updateChildren(newChatMap);

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("user");
        userDB.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        userDB.child(userList.get(position).getUid()).child("chat").child(key).setValue(true);
    }*/


    class UserListViewHolder extends RecyclerView.ViewHolder {

        TextView mName, mPhone, mStatus;
        ImageView mImage;
        LinearLayout mLayout;
        CheckBox mAdd;
        FindUserActivity findUserActivity;

        UserListViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            mStatus = view.findViewById(R.id.status);
            mImage = view.findViewById(R.id.image);
            mLayout = view.findViewById(R.id.userLayout);
            mAdd = view.findViewById(R.id.add);
//            this.findUserActivity = findUserActivity;
//            mLayout.setOnLongClickListener(findUserActivity);
        }
    }
}