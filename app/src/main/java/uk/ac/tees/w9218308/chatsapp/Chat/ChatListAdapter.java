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

import java.util.ArrayList;

import uk.ac.tees.w9218308.chatsapp.ChatActivity;
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
        holder.mTitle.setText(chatList.get(position).getChatId());
        /*holder.mPhone.setText(chatList.get(position).getPhone());
        holder.mStatus.setText(chatList.get(position).getStatus());
        holder.mImage.setImageBitmap(chatList.get(position).getImageUrl());*/

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

        TextView mTitle/*, mPhone, mStatus*/;
        //        public ImageView mImage;
        LinearLayout mLayout;

        ChatListViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.chatTitle);
            /*mPhone = view.findViewById(R.id.phone);
            mStatus = view.findViewById(R.id.status);
            mImage = view.findViewById(R.id.image);*/
            mLayout = view.findViewById(R.id.userLayout);
        }
    }
}