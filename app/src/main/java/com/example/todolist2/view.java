package com.example.todolist2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist2.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class view extends AppCompatActivity {

    TextView edit, cancel, delete, update, t_date, list_title;
    EditText task_title, task_description;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference listRef;
    private DatabaseReference taskRef;
    private String uid;
    private Task task = new Task("waiting", "waiting", "waiting", false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        edit = findViewById(R.id.edit);
        cancel = findViewById(R.id.cancel);
        delete = findViewById(R.id.delete_task);
        update = findViewById(R.id.update_task);
        task_title = findViewById(R.id.a_task_title);
        task_description = findViewById(R.id.task_description);
        t_date = findViewById(R.id.task_date);
        list_title = findViewById(R.id.z_list_title);



        updateUI(task);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(view.this, Login.class);
            startActivity(intent);
            finish();
        }
        uid = currentUser.getUid();

        String listId = getIntent().getStringExtra("listId");
        String taskId = getIntent().getStringExtra("taskId");
        listRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("lists").child(listId);
        taskRef = listRef.child("tasks").child(taskId);
        listRef.child("title").get().addOnSuccessListener(dataSnapshot -> list_title.setText((String) dataSnapshot.getValue()));
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                task = dataSnapshot.getValue(Task.class);
                if (task != null) updateUI(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ImageButton btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });

        cancelEdit();

        edit.setOnClickListener(view -> {
            openEdit();
        });
        cancel.setOnClickListener(view -> {
            cancelEdit();
        });
        delete.setOnClickListener(view -> {
            taskRef.removeValue();
            onBackPressed();
            Toast.makeText(view.this, "deleted successfully", Toast.LENGTH_SHORT).show();
        });
        update.setOnClickListener(view -> {
            updateData();
            cancelEdit();
            Toast.makeText(view.this, "updated successfully", Toast.LENGTH_SHORT).show();
        });

    }

    private void updateUI(Task task) {
        task_title.setText(task.getTitle());
        task_description.setText(task.getDescription());
        t_date.setText(task.getDate());
    }

    public void openEdit() {
        edit.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
        task_title.setEnabled(true);
        task_description.setEnabled(true);
        task_title.setBackgroundResource(R.drawable.view_background);
        task_description.setBackgroundResource(R.drawable.view_background);
        t_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_edit, 0);
    }

    public void cancelEdit() {
        edit.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);
        update.setVisibility(View.GONE);
        task_title.setEnabled(false);
        task_description.setEnabled(false);
        task_title.setBackgroundColor(Color.WHITE);
        task_description.setBackgroundColor(Color.WHITE);
        t_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        updateUI(task);
    }

    private void updateData() {
        String title = task_title.getText().toString().trim();
        String description = task_description.getText().toString().trim();
        String date = t_date.getText().toString().trim();
        if (title.isEmpty()) {
            task_title.setError("Please enter title");
            return;
        }
        if (description.isEmpty()) {
            task_description.setError("Please enter description");
            return;
        }
        if (date.isEmpty()) {
            t_date.setError("Please enter password");
            return;
        }
        task.setTitle(title);
        task.setDescription(description);
        task.setDate(date);
        taskRef.setValue(task);
    }

}