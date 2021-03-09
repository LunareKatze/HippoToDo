package com.lunarekatze.hippotodo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lunarekatze.hippotodo.db.TaskHelper;         // подключаем

import java.util.ArrayList;

// ОСНОВНОЙ КЛАСС

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener
{
    MyAdapter adapter;
    private TaskHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        ArrayList<String> animalNames = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);*/

        mHelper = new TaskHelper(this);
        updateUI();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater(). inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_task:
                final EditText taskEditText = new EditText (this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("New Task")
                    .setMessage("Add a new task")
                    .setView (taskEditText)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            String task = String.valueOf(taskEditText.getText());
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                            db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                            updateUI();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void updateUI(){

        //RecyclerView recyclerView = findViewById(R.id.list_todo);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adapter.setClickListener(this);
        //recyclerView.setAdapter(adapter);

        RecyclerView recyclerView = findViewById(R.id.list_todo);   // Инициализация RecycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);   // Устанавливаем LayoutManager для RecycleView чтобы можно было управлять видом

        ArrayList<String> taskList = new ArrayList<>();     // Создаём массив типа String
        SQLiteDatabase db = mHelper.getReadableDatabase();  // Создаём и/или открываем базу на чтение
        Cursor cursor = db.query(TaskHelper.DATABASE_TABLE,
                    new String[] {TaskHelper.COLUMN_ID, TaskHelper.COLUMN_NAME}, null, null, null, null, null);
        while (cursor.moveToNext()){    // Пока у курсора есть следующий элемент...
            int index = cursor.getColumnIndex(TaskHelper.COLUMN_NAME);  // Получаем индекс для каждого элемента в базе в таблице COLUMN_NAME
            taskList.add(cursor.getString(index));  // Получаем строку по индексу и пишем её в массив
        }
        if(adapter == null){
            adapter = new MyAdapter(this, taskList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            //mAdapter = new ArrayAdapter<>(this,R.layout.item_todo, R.id.task_title, taskList);
            //mTaskListView.setAdapter(mAdapter);
            //mTaskListView.setAdapter(MyAdapter.MyViewHolder.class);
        }
        else {
            adapter.clear();
            //adapter (this, "");
            //adapter.clear;
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public void changeStatusTask(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        CheckBox checkBox = parent.findViewById(R.id.task_delete);
        checkBox.setChecked(false);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        //int intStatus = TaskHelper
        db.delete(TaskHelper.DATABASE_TABLE, TaskHelper.COLUMN_NAME + " =?", new String[] {task});
        db.close();
        updateUI();
    }

    public void changeStatusTaskOld(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        //int intStatus = TaskHelper
        db.delete(TaskHelper.DATABASE_TABLE, TaskHelper.COLUMN_NAME + " =?", new String[] {task});
        db.close();
        updateUI();
    }

    public void addTask(View view) {
        final EditText task_text = new EditText (this);
        task_text.setSingleLine();

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        task_text.setLayoutParams(params);
        container.addView(task_text);

        //AlertDialog alertDialog = new AlertDialog(this, R.style.Theme_MaterialComponents_Dialog_Alert);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("New Task")
                .setMessage("Add a new task")
                //.setView (task_text)
                .setView(container)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        final AlertDialog alertDialog = dialog.show();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = String.valueOf(task_text.getText());

                SQLiteDatabase database = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();         // Класс исп-ся для добавления новых строк в таблицу

                /*Cursor cursor = database.query(TaskHelper.DATABASE_TABLE,null, TaskHelper.COLUMN_NAME,
                        null, null, null, null, null);*/

                Cursor cursor2 = database.rawQuery("SELECT * FROM " + TaskHelper.DATABASE_TABLE + " WHERE " + TaskHelper.COLUMN_NAME + " = '" + task + "'", null);
                int count = cursor2.getCount();
                cursor2.close();
                if (task.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field is empty", Toast.LENGTH_LONG).show();
                }
                else {
                    if (count > 0) {
                        Toast.makeText(getApplicationContext(), "string found!!! =)", Toast.LENGTH_LONG).show();
                    }
                    else {
                        values.put(TaskHelper.COLUMN_NAME, task);
                        values.put(TaskHelper.COLUMN_STATUS, "current");
                        database.insertWithOnConflict(TaskHelper.DATABASE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        database.close();
                        alertDialog.dismiss();
                        updateUI();
                    }
                }
            }
        });
    }

    //public String checkTask ()
}
