package uk.ac.tees.w9218308.chatsapp.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import uk.ac.tees.w9218308.chatsapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    //    Context context;
    ArrayList<MessageObject> messageList;

    /*public MessageAdapter(Context context, ArrayList<ChatObject> message) {
        this.context = context;
        this.Message = Message;
    }*/

    public MessageAdapter(ArrayList<MessageObject> message) {
        this.messageList = message;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        MessageViewHolder rcv = new MessageViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        holder.mMessage.setText(messageList.get(position).getMessage());
        holder.mSender.setText(messageList.get(position).getSenderId());

        if (messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
            holder.mViewMedia.setVisibility(View.GONE);

        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                        .setStartPosition(0)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage, mSender;
        LinearLayout mLayout;
        Button mViewMedia;

        MessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.messageList);
            mSender = view.findViewById(R.id.sender);
            mLayout = view.findViewById(R.id.userLayout);
            mViewMedia = view.findViewById(R.id.viewMedia);
        }
    }
}