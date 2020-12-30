package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist2.Adpters.TaskAdpter;
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


public class tasks extends AppCompatActivity {

    EditText create_task;
    TextView listTitle;
    RecyclerView taskRecycler;
    TaskAdpter tasksAdapter;
    TList todoList = new TList();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference listRef;
    private String uid;
    private TextView delete_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(tasks.this, Login.class);
            startActivity(intent);
            finish();
        }
        uid = currentUser.getUid();

        String listId = getIntent().getStringExtra("listId");
        listRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("lists").child(listId);

        taskRecycler = findViewById(R.id.taskRecycler);
        ListPaddingDecoration dividerItemDecoration = new ListPaddingDecoration(this);
        taskRecycler.addItemDecoration(dividerItemDecoration);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TaskAdpter(tasks.this, todoList, listRef);
        taskRecycler.setAdapter(tasksAdapter);

        create_task = findViewById(R.id.create_task);
        create_task.setOnEditorActionListener((view, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                Helpers.HideKeyboard(tasks.this);
                String titleText = create_task.getText().toString().trim();
                if (titleText.isEmpty()) {
                    create_task.setError("please enter title");
                    return false;
                } else {
                    AddTask(titleText);
                    create_task.getText().clear();
                }
            }
            return true;
        });

        listTitle = findViewById(R.id.listTitle);
        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;
                todoList.setId((String) dataSnapshot.child("id").getValue());
                todoList.setTitle((String) dataSnapshot.child("title").getValue());
                todoList.getTasks().clear();
                if (dataSnapshot.child("tasks").exists()) {
                    for (DataSnapshot tasksSnapshot : dataSnapshot.child("tasks").getChildren()) {
                        todoList.getTasks().add(tasksSnapshot.getValue(Task.class));
                    }
                }
                listTitle.setText(todoList.getTitle() + " List");
                tasksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        delete_list = findViewById(R.id.delete_list);
        delete_list.setOnClickListener(view -> {
            listRef.removeValue();
            onBackPressed();
        });
        ImageButton btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });


    }

    private void AddTask(String titleText) {
        String taskId = listRef.child("tasks").push().getKey();
        Task newTask = new Task(taskId, titleText, "2020-20-20 : 20:20", "desc", false);
        listRef.child("tasks").child(taskId).setValue(newTask);
        Toast.makeText(tasks.this, "to-do task has been added successfully", Toast.LENGTH_SHORT).show();
    }

}