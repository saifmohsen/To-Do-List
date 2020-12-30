package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
    EditText create_list;
    RecyclerView listRecycler;
    ListAdapter listsAdapter;
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
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
        }
        uid = currentUser.getUid();
        listsRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("lists");

        listRecycler = findViewById(R.id.ListRV);
        ListPaddingDecoration dividerItemDecoration = new ListPaddingDecoration(this);
        listRecycler.addItemDecoration(dividerItemDecoration);
        listRecycler.setLayoutManager(new LinearLayoutManager(this));
        listsAdapter = new ListAdapter(Home.this, lists);
        listRecycler.setAdapter(listsAdapter);

        create_list = findViewById(R.id.create_list_category);

        create_list.setOnEditorActionListener((view, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                Helpers.HideKeyboard(Home.this);
                String titleText = create_list.getText().toString().trim();
                if (titleText.isEmpty()) {
                    create_list.setError("please enter title");
                    return false;
                } else {
                    AddList(titleText);
                    create_list.getText().clear();
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

                listsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        TextView tv_Logout = findViewById(R.id.logout);
        tv_Logout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            onBackPressed();
        });

    }

    public void AddList(String titleText) {
        String listId = listsRef.push().getKey();
        TList newList = new TList(listId, titleText);
        listsRef.child(listId).setValue(newList);
        Toast.makeText(Home.this, "to-do list has been added successfully", Toast.LENGTH_SHORT).show();
    }


}
