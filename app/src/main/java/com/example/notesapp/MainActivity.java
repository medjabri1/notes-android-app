package com.example.notesapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.notesapp.Adapters.NotesAdapter;
import com.example.notesapp.Classes.Note;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity {

    private static Context CONTEXT;

    NotesAdapter notesAdapter;

    private static RecyclerView recyclerView;
    private static LinearLayout no_notes_layout;

    private ImageButton grid_list_button;
    private ImageButton add_new_note_button;

    private static int COLUMN_NBR = 2;
    private static List<Note> notes = new ArrayList<Note>(0);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CONTEXT = MainActivity.this;

        add_new_note_button = findViewById(R.id.add_new_note_button);
        recyclerView = findViewById(R.id.notes_recyclerview);
        no_notes_layout = findViewById(R.id.no_notes_layout);
        grid_list_button = findViewById(R.id.grid_list_button);
        
        playAnimations();
        displayNotes();
    }

    private void playAnimations() {

        add_new_note_button.setAlpha(0f);
        add_new_note_button.setScaleX(0f);
        add_new_note_button.setScaleY(0f);
        add_new_note_button.setRotation(200);
        add_new_note_button.setTranslationY(200);


        add_new_note_button.animate().alpha(1f).setStartDelay(1000).setDuration(250).start();
        add_new_note_button.animate().scaleX(1f).setStartDelay(1000).setDuration(300).start();
        add_new_note_button.animate().scaleY(1f).setStartDelay(1000).setDuration(300).start();
        add_new_note_button.animate().rotation(0).setStartDelay(1000).setDuration(250).start();
        add_new_note_button.animate().translationY(0).setStartDelay(1000).setDuration(250).start();
    }

    public void changeLayout(View v) {
        if(COLUMN_NBR == 2) {
            grid_list_button.setImageResource(R.drawable.ic_list);
            COLUMN_NBR = 1;
        } else {
            grid_list_button.setImageResource(R.drawable.ic_grid);
            COLUMN_NBR = 2;
        }
        displayNotes();
    }

    public void displayNotes() {
        notesAdapter = new NotesAdapter(this, notes);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, COLUMN_NBR));
        recyclerView.setAdapter(notesAdapter);

        if(notes.size() == 0){
            no_notes_layout.setVisibility(View.VISIBLE);
        } else {
            no_notes_layout.setVisibility(View.GONE);
        }
    }

    public static void noteItemClick(int note_id) {

        Note note = null;
        for(int i=0; i<notes.size(); i++) if(notes.get(i).getId()==note_id) note = notes.get(i);

        if(note != null) {
            Intent intent = new Intent(CONTEXT, NoteActivity.class);
            intent.putExtra("note_state", "old");
            intent.putExtra("note_id", String.valueOf(note.getId()));
            intent.putExtra("note_content", note.getContent());
            CONTEXT.startActivity(intent);

        } else {
            showToast("Error: note not found!");
        }
    }

    public static void noteItemLongPress(final int note_id) {

        Note note = null;
        for(int i=0; i<notes.size(); i++) if(notes.get(i).getId()==note_id) note = notes.get(i);

        if(note != null) {
            new AlertDialog.Builder(CONTEXT)
                    .setTitle("Cancel")
                    .setMessage("Are you sure you want to delete this note?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteNote(note_id);
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            showToast("Error: note not found!");
        }
    }

    public void addButtonClick(View v) {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra("note_state", "new");
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addNewNote(final String content) {

        int id = notes.size() > 0 ? notes.get(0).getId() + 1 : 0;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String current_time = dtf.format(now);

        notes.add(0, new Note(id, content, current_time));

        Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(0);
        no_notes_layout.setVisibility(View.GONE);

        /*final Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CONTEXT, content, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public static void deleteNote(int note_id) {
        for (int i=0; i<notes.size(); i++) {
            if(notes.get(i).getId()==note_id) {
                Note remove = notes.remove(i);
                showToast("Note deleted");
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(i);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateNote(int note_id, String content) {
        for(int i=0; i<notes.size(); i++) {
            if(notes.get(i).getId() == note_id) {
                if(!notes.get(i).getContent().equals(content)) {
                    //Content is updated
                    String current_time = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(LocalDateTime.now());
                    notes.get(i).setContent(content);
                    notes.get(i).setDate(current_time);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(i);
                    showToast("Note updated");
                } else {
                    //No changes in the note content
                    showToast("No changes detected");
                }
            }
        }
    }

    public static void showToast(String text) {
        Toast.makeText(CONTEXT, text, Toast.LENGTH_SHORT).show();
    }
}
