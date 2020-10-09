package com.example.notesapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Classes.Note;
import com.example.notesapp.MainActivity;
import com.example.notesapp.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Note> mData;

    public NotesAdapter(Context mContext, List<Note> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.note_item_layout, parent, false);

        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final int MAX_CONTENT = 60;
        String content = String.valueOf(mData.get(position).getContent());

        content = content.length() > MAX_CONTENT ? content.substring(0, MAX_CONTENT) + ".." : content;
        final int note_id = mData.get(position).getId();

        holder.tv_title.setText(content);
        holder.tv_date.setText(String.valueOf(mData.get(position).getDate()));
        holder.tv_id.setText(String.valueOf(note_id));

        int ANIM_DELAY =  50 * position;

        holder.itemView.setAlpha(0.5f);
        //holder.itemView.setTranslationY(30);

        holder.itemView.animate().alpha(1f).setDuration(300).setStartDelay(ANIM_DELAY).start();
        //holder.itemView.animate().translationY(0).setDuration(300).setStartDelay(ANIM_DELAY).start();

        //Element click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.noteItemClick(note_id);
            }
        });

        //Element long press listener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity.noteItemLongPress(note_id);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_date;
        private TextView tv_id;
        private View itemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.note_title);
            tv_date = itemView.findViewById(R.id.note_date);
            tv_id = itemView.findViewById(R.id.note_id);
            this.itemView = itemView;
        }
    }
}
