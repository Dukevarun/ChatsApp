package uk.ac.tees.w9218308.chatsapp.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        /*TextView mName, mMessage;
        CircleImageView mImage;*/
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