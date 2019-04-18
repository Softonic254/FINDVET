package com.example.user.locvet.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.locvet.R;
import com.example.user.locvet.models.Vets;

import java.util.List;

public class VetsAdapter extends RecyclerView.Adapter<VetsAdapter.VetViewHolder> {

    private Context mContext;
    private List<Vets> vets;

    public VetsAdapter(Context mContext, List<Vets> vets) {
        this.mContext = mContext;
        this.vets = vets;
    }

    @NonNull
    @Override
    public VetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vet, parent, false);
        return new VetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VetViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.vets.size();
    }

    class VetViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView name;
        private AppCompatTextView phone;

        public VetViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }

        public void bind(int position) {
            final Vets vet = vets.get(position);

            name.setText(vet.getName());
            phone.setText("07"+ vet.getContact());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Vet details")
                            .setMessage(vet.getName()+"\n\n07" +vet.getContact() +"\n\n"+ vet.getEmail())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            });
        }
    }
}
