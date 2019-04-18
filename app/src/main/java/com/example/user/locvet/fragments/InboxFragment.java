package com.example.user.locvet.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.locvet.R;
import com.example.user.locvet.models.Requests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {

    private SwipeRefreshLayout refreshInbox;
    private ListView listNotifications;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayAdapter<String> notificationsAdapter;
    private List<String> notifications;

    public InboxFragment() {
        // Required empty public constructor
    }

    public static InboxFragment newInstance() {
        InboxFragment fragment = new InboxFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        notifications = new ArrayList<>();
        notificationsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notifications);

        refreshInbox = view.findViewById(R.id.refreshMessages);
        listNotifications = view.findViewById(R.id.inboxVets);

        refreshInbox.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifications();
            }
        });

        listNotifications.setAdapter(notificationsAdapter);

        fetchNotifications();

        return view;
    }

    private void fetchNotifications() {
        refreshInbox.setRefreshing(true);
        db.collection("requests")
                .whereEqualTo("farmerId", mAuth.getCurrentUser().getUid())
                .whereEqualTo("booked", true)
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Requests requests = document.toObject(Requests.class);
                                notifications.add(requests.getRequest() + " has been booked");
                                notificationsAdapter.notifyDataSetChanged();
                            }
                            refreshInbox.setRefreshing(false);
                        }
                    }
                });
    }

}
