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
import com.example.user.locvet.adapters.ClientsAdapter;
import com.example.user.locvet.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClientsFragment extends Fragment {

    private SwipeRefreshLayout refreshClients;
    RecyclerView listClients;

    private FirebaseFirestore db;
    private List<User> users;
    private ClientsAdapter clientsAdapter;

    public ClientsFragment() {
        // Required empty public constructor
    }

    public static ClientsFragment newInstance() {
        ClientsFragment fragment = new ClientsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        db = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        clientsAdapter = new ClientsAdapter(getActivity(), users);

        refreshClients = view.findViewById(R.id.refreshClients);
        listClients = view.findViewById(R.id.listClients);

        refreshClients.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        refreshClients.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchClients();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listClients.setLayoutManager(layoutManager);
        listClients.setItemAnimator(new DefaultItemAnimator());
        listClients.setAdapter(clientsAdapter);

        fetchClients();

        return view;
    }

    private void fetchClients() {
        refreshClients.setRefreshing(true);
        db.collection("users")
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int clientSize = users.size();
                            users.clear();
                            clientsAdapter.notifyItemRangeRemoved(0, clientSize);

                            for (DocumentSnapshot document : task.getResult()) {
                                User client = document.toObject(User.class);
                                users.add(client);
                                clientsAdapter.notifyDataSetChanged();
                            }
                        }

                        refreshClients.setRefreshing(false);
                    }
                });
    }

}
