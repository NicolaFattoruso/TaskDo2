package com.example.android.taskdo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    List<Task> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Database
        //Database?!
        final AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries().build();

        //Loads the tasks from the database
        taskList = db.TaskDao().getAllTasks();

        //FAB to remove all tasks
        FloatingActionButton removeTasksFab = findViewById(R.id.remove_tasks_fab);

        //Sets a click Listener on the RemoveTaskFab and calls showAlertNukeButtonClicked()
        removeTasksFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shows the user an Alert before deleting all items from the list
                showAlertNukeButtonClicked(view, db);
            }
        });

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

    }


    /**
     * Requests a confirmation from the user before deleting everything
     *
     * @param view view?
     * @param db          is the database where all entries are stored
     *
     */
    public void showAlertNukeButtonClicked(View view, final AppDatabase db) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Set the title and message of the dialog
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.nukeConfirmation));
        // add positive and negative buttons and set corresponding actions
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Deletes all items from the List
                taskList.clear();
                //Deletes all entries in the Database
                db.TaskDao().nukeTable();
            }
        });
        //Negative button does nothing
        builder.setNegativeButton(getString(R.string.cancel), null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}


