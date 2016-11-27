package com.example.android.activeagenda;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MenuBarActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);

        dbHelper = new DBHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_manage_tags:
                intent = new Intent(this, ManageTagsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_export_tasks_to_phone_cal:
                exportAllTasksToCalendar();
                return true;

            case R.id.action_day_view:
                intent = new Intent(this, DayViewActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_planner_view:
                intent = new Intent(this, PlannerViewActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportAllTasksToCalendar() {
        List<Task> allTasks = dbHelper.getAllTasks();
        for (int i = 0; i < allTasks.size(); i++) {
            Task curTask = allTasks.get(i);
            Intent calIntent = new Intent(Intent.ACTION_EDIT)
                    .setType("vnd.android.cursor.item/event")
                    .putExtra(CalendarContract.Events.TITLE, curTask.name)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, curTask.dueDate)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, curTask.dueDate)
                    .putExtra(CalendarContract.Events.ALL_DAY, false)
                    .putExtra(CalendarContract.Events.ALL_DAY, curTask.description);
            startActivity(calIntent);
        }
    }
}
