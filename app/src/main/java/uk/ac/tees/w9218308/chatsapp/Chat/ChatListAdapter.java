package uk.ac.tees.w9218308.chatsapp.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.w9218308.chatsapp.ChatActivity;
import uk.ac.tees.w9218308.chatsapp.ChatFragment;
import uk.ac.tees.w9218308.chatsapp.R;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context context;
    private ArrayList<ChatObject> chatList;

    public ChatListAdapter(Context context, ArrayList<ChatObject> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new ChatListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {

        /*holder.mName.setText(chatList.get(position).getChatName());
        holder.mMessage.setText(chatList.get(position).getChatMessage());
        if (chatList.get(position).getChatImage().equals("default"))
            holder.mImage.setImageResource(R.drawable.profile_image);
        else
            Glide.with(context).load(chatList.get(position).getChatImage()).into(holder.mImage);*/

        holder.mTitle.setText(chatList.get(position).getChatId());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatObject", chatList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView mName, mMessage;
        CircleImageView mImage;
        LinearLayout mLayout;
        public TextView mTitle;

        ChatListViewHolder(View view) {
            super(view);
            /*mName = view.findViewById(R.id.chatName);
            mMessage = view.findViewById(R.id.chatMessage);
            mImage = view.findViewById(R.id.chatImage);*/
            mLayout = view.findViewById(R.id.chatListLayout);
            mTitle = view.findViewById(R.id.title);
        }
    }
}