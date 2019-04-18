package com.example.user.locvet.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.locvet.R;
import com.example.user.locvet.models.VetChat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private Context mContext;
    private List<VetChat> vetChats;

    public InboxAdapter(Context mContext, List<VetChat> vetChats) {
        this.mContext = mContext;
        this.vetChats = vetChats;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.InboxViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.vetChats.size();
    }

    class InboxViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView name;
        private AppCompatTextView message;
        private AppCompatTextView time;

        public InboxViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.text_message_time);
        }

        public void bind(int position) {
            final VetChat vetChat = vetChats.get(position);

            name.setText(vetChat.getName());
            message.setText(vetChat.getMessage());

            if (vetChat.getDate() != null)
                time.setText(new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault()).format(vetChat.getDate()));
            else
                time.setText(new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault()).format(new Date()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}

