package com.example.notesapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    private EditText new_note_et;
    private ImageButton delete_button;

    private String note_state = "new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        new_note_et = findViewById(R.id.new_note_et);
        delete_button = findViewById(R.id.delete_button);

        checkIntent();
    }

    private void checkIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("note_state")) {
            if(Objects.equals(intent.getStringExtra("note_state"), "old")) {
                //Old note - Update note
                new_note_et.setText(intent.getStringExtra("note_content"));
                delete_button.setVisibility(View.VISIBLE);
            } else {
                delete_button.setVisibility(View.GONE);
            }
            note_state = intent.getStringExtra("note_state");
        }
    }

    public void backButtonClick(View view) {
        String content = new_note_et.getText().toString();

        if(note_state.equals("old")) {
            String intent_content = getIntent().getStringExtra("note_content");
            if(content.equals(intent_content)) {
                finish();
                return;
            }
        }

        if(content.trim().length() > 0) {
            new AlertDialog.Builder(NoteActivity.this)
                    .setTitle("Cancel")
                    .setMessage("Changes will not be saved, continue?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(NoteActivity.this, "Changes not saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveButtonClick(View view) {
        String content = new_note_et.getText().toString();
        if(content.trim().length() == 0) {
            Toast.makeText(NoteActivity.this, "Content could not be empty", Toast.LENGTH_SHORT).show();
        } else {
            if(note_state.equals("old")) {
                Intent intent = getIntent();
                if(intent.getStringExtra("note_id") != null) {
                    int note_id = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("note_id")));
                    MainActivity.updateNote(note_id, content);
                } else {
                    Toast.makeText(NoteActivity.this, "Note id not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                MainActivity.addNewNote(content);
            }
        }
        finish();
    }

    public void deleteButtonClick(View v) {
        if(note_state.equals("old")) {
            Intent intent = getIntent();
            if(intent.getStringExtra("note_id") != null) {
                int note_id = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("note_id")));
                MainActivity.deleteNote(note_id);
            } else {
                Toast.makeText(NoteActivity.this, "Note id not found", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
