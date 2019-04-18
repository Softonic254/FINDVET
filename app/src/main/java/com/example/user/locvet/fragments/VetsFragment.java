package com.example.user.locvet.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.locvet.R;
import com.example.user.locvet.adapters.VetsAdapter;
import com.example.user.locvet.models.Vets;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VetsFragment extends Fragment {

    private SwipeRefreshLayout refreshVets;
    private FirebaseFirestore db;
    private List<Vets> vets;
    private VetsAdapter vetsAdapter;

    public VetsFragment() {
        // Required empty public constructor
    }

    public static VetsFragment newInstance() {
        VetsFragment fragment = new VetsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            db = FirebaseFirestore.getInstance();
            vets = new ArrayList<>();
            vetsAdapter = new VetsAdapter(getActivity(), vets);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vets, container, false);

        refreshVets = view.findViewById(R.id.refreshVets);
        RecyclerView listVets = view.findViewById(R.id.listVets);

        refreshVets.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        refreshVets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchVets();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listVets.setLayoutManager(layoutManager);
        listVets.setItemAnimator(new DefaultItemAnimator());
        listVets.setAdapter(vetsAdapter);

        fetchVets();

        return view;
    }

    private void fetchVets() {
        refreshVets.setRefreshing(true);
        db.collection("vets")
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int vetsSize = vets.size();
                            vets.clear();
                            vetsAdapter.notifyItemRangeRemoved(0, vetsSize);

                            for (DocumentSnapshot document : task.getResult()) {
                                Vets vet = document.toObject(Vets.class);
                                vets.add(vet);
                                vetsAdapter.notifyDataSetChanged();
                            }
                        }

                        refreshVets.setRefreshing(false);
                    }
                });
    }

}
