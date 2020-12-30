package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist2.Adpters.ListAdapter;
import com.example.todolist2.models.TList;
import com.example.todolist2.models.Task;
import com.example.todolist2.utilities.Helpers;
import com.example.todolist2.utilities.ListPaddingDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class Home extends AppCompatActivity {

    public ArrayList<TList> lists = new ArrayList<>();
    EditText et_list_create;
    RecyclerView listRecycler;
    ListAdapter listsRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference listsRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(Home.this, SignUp.class);
            startActivity(intent);
            finish();
        }
        uid = currentUser.getUid();
        listsRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("lists");

        listRecycler = findViewById(R.id.ListRV);
        ListPaddingDecoration dividerItemDecoration = new ListPaddingDecoration(this);
        listRecycler.addItemDecoration(dividerItemDecoration);
        listRecycler.setLayoutManager(new LinearLayoutManager(this));
        listsRecyclerAdapter = new ListAdapter(Home.this, lists);
        listRecycler.setAdapter(listsRecyclerAdapter);

        et_list_create = findViewById(R.id.create_list_category);
        et_list_create.setOnEditorActionListener((view, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                Helpers.HideKeyboard(Home.this);
                String titleText = et_list_create.getText().toString().trim();
                if (titleText.isEmpty()) {
                    et_list_create.setError("please enter title");
                    return false;
                } else {
                    AddTODOList(titleText);
                    et_list_create.getText().clear();
                }
            }
            return true;
        });


        listsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TList todoList = new TList();

                    todoList.setId((String) snapshot.child("id").getValue());
                    todoList.setTitle((String) snapshot.child("title").getValue());
                    if (snapshot.child("tasks").exists()) {
                        for (DataSnapshot tasksSnapshot : snapshot.child("tasks").getChildren()) {
                            todoList.getTasks().add(tasksSnapshot.getValue(Task.class));
                        }
                    }

                    lists.add(todoList);
                }
                listsRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        ImageButton btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });

        TextView tv_Logout = findViewById(R.id.logout);
        tv_Logout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            onBackPressed();
        });

    }

    public void AddTODOList(String titleText) {
        String listId = listsRef.push().getKey();
        TList newList = new TList(listId, titleText);
        listsRef.child(listId).setValue(newList);
        Toast.makeText(Home.this, "to-do list has been added successfully", Toast.LENGTH_SHORT).show();
    }



}