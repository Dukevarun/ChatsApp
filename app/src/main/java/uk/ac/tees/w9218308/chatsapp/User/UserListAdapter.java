package uk.ac.tees.w9218308.chatsapp.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import uk.ac.tees.w9218308.chatsapp.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private Context context;
    private ArrayList<UserObject> userList;

    public UserListAdapter(Context context,ArrayList<UserObject> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());
        holder.mStatus.setText(userList.get(position).getStatus());
        if (userList.get(position).getImageUrl().equals("default"))
            holder.mImage.setImageResource(R.drawable.profile_image);
        else
            Glide.with(context).load(userList.get(position).getImageUrl()).into(holder.mImage);


        /*holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userObject", userList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });*/

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

    class UserListViewHolder extends RecyclerView.ViewHolder {

        TextView mName, mPhone, mStatus;
        CircleImageView mImage;
        CheckBox mAdd;
        LinearLayout mLayout;

        UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.userName);
            mPhone = itemView.findViewById(R.id.userPhone);
            mStatus = itemView.findViewById(R.id.userStatus);
            mImage = itemView.findViewById(R.id.userImage);
            mAdd = itemView.findViewById(R.id.add);
            mLayout = itemView.findViewById(R.id.userLayout);
        }
    }
}
