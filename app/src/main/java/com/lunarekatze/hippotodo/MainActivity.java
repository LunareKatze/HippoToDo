package com.lunarekatze.hippotodo;

import android.content.ContentValues;
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

import com.lunarekatze.hippotodo.db.TaskHelper;

import java.util.ArrayList;

// ОСНОВНОЙ КЛАСС

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {
    MyAdapter adapter;
    private TaskHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new TaskHelper(this);
        updateUI();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void updateUI() {
        RecyclerView recyclerView = findViewById(R.id.list_todo);   // Инициализация RecycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);   // Устанавливаем LayoutManager для RecycleView чтобы можно было управлять видом
        ArrayList<String> taskList = new ArrayList<>();     // Создаём ArrayList типа String
        ArrayList<String> statusList = new ArrayList<>();     // Создаём ArrayList типа String
        SQLiteDatabase db = mHelper.getReadableDatabase();  // Создаём и/или открываем базу на чтение
        Cursor cursor = db.query(TaskHelper.DATABASE_TABLE,
                    new String[] {TaskHelper.COLUMN_ID, TaskHelper.COLUMN_NAME, TaskHelper.COLUMN_STATUS}, null, null, null, null, null);
        while (cursor.moveToNext()){    // Пока у курсора есть следующий элемент...
            int index = cursor.getColumnIndex(TaskHelper.COLUMN_NAME);  // Получаем индекс для каждого элемента в базе в столбце COLUMN_NAME
            taskList.add(cursor.getString(index));  // Получаем строку по индексу и пишем её в ArrayList
            int indexStatus = cursor.getColumnIndex(TaskHelper.COLUMN_STATUS);  // Получаем индекс для каждого элемента в базе в столбце COLUMN_STATUS
            statusList.add(cursor.getString(indexStatus));  // Получаем строку по индексу и пишем её в ArrayList
        }
        if(adapter == null) {                   // если адаптер пустой
            adapter = new MyAdapter(this, taskList, statusList);        // Передаём в адаптер ArrayList
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),              // линии
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        else {
            adapter.clear();
            adapter.addAll(taskList, statusList);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public void changeStatusTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        CheckBox checkBox = parent.findViewById(R.id.change_status);
        ContentValues values = new ContentValues();         // Класс исп-ся для добавления новых строк в таблицу
        if (checkBox.isChecked()){
            values.put("status", "done");
        }
        else {
            values.put("status", "current");
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.update(TaskHelper.DATABASE_TABLE, values, TaskHelper.COLUMN_NAME + "= ?", new String[] { taskTextView.getText().toString() });
        db.close();
        updateUI();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);       //пишем в переменную текст задачи
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
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

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setView(container)
                .setPositiveButton(R.string.dialog_positive, null)
                .setNegativeButton(R.string.dialog_negative, null)
                .create();

        final AlertDialog alertDialog = dialog.show();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = String.valueOf(task_text.getText());
                SQLiteDatabase database = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();         // Класс исп-ся для добавления новых строк в таблицу
                Cursor cursor = database.rawQuery("SELECT * FROM " + TaskHelper.DATABASE_TABLE + " WHERE " + TaskHelper.COLUMN_NAME + " = '" + task + "'", null);   //ищем в базе такую задачу
                int count = cursor.getCount();      //пишем в int номер строки задачи
                cursor.close();
                if (task.equals("")) {              // если user не ввел название задачи, то
                    Toast.makeText(getApplicationContext(), R.string.toast_field_empty, Toast.LENGTH_LONG).show();
                }
                else {
                    if (count > 0) {                // если номер строки есть, то такая задача уже есть
                        Toast.makeText(getApplicationContext(), R.string.toast_same_task, Toast.LENGTH_LONG).show();
                    }
                    else {                      // иначе это новая задача
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
}
