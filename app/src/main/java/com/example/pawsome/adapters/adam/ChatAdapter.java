package com.example.pawsome.adapters.adam;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsome.current.CurrentUser;
import com.example.pawsome.utils.Constants;
import com.example.pawsome.databinding.ReceivedMessageItemBinding;
import com.example.pawsome.databinding.SentMessageItemBinding;
import com.example.pawsome.model.adam_delete.ChatMessage;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatMessage> chatMessages;

    public ChatAdapter() {
        this.chatMessages = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    SentMessageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false)
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ReceivedMessageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == Constants.VIEW_TYPE_SENT)
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        else
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderID().equals(CurrentUser.getInstance().getUid()))
            return Constants.VIEW_TYPE_SENT;
        else
            return Constants.VIEW_TYPE_RECEIVED;
    }

    public void updateMessages(ArrayList<ChatMessage> chatMessages){
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final SentMessageItemBinding binding;

        public SentMessageViewHolder(SentMessageItemBinding sentMessageItemBinding) {
            super(sentMessageItemBinding.getRoot());
            this.binding = sentMessageItemBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.sentMessage.setText(chatMessage.getMessage());
            binding.sentDateTime.setText(chatMessage.getDateTime());
            binding.sentName.setText(chatMessage.getSenderName());
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ReceivedMessageItemBinding binding;

        public ReceivedMessageViewHolder(ReceivedMessageItemBinding receivedMessageItemBinding) {
            super(receivedMessageItemBinding.getRoot());
            this.binding = receivedMessageItemBinding;
        }

        private void setData(ChatMessage chatMessage) {
            binding.receivedMessage.setText(chatMessage.getMessage());
            binding.receivedDateTime.setText(chatMessage.getDateTime());
            binding.receivedName.setText(chatMessage.getSenderName());
        }
    }
}
