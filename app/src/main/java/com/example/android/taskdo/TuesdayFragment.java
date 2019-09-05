package com.example.android.taskdo;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TuesdayFragment extends Fragment {
    private static final String TAG = "MondayFragment";

    List<Task> taskList;


    public TuesdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.task_list, container, false);

        //Database?!
        final AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "tuesdayDb")
                .allowMainThreadQueries().build();

        //Loads the tasks from the database
        taskList = db.TaskDao().getAllTasks();


        /*
         * Create a {@link TaskAdapter}, whose data source is a list of
         {@link Task}s. The adapter knows how to create list item views for each item
         in the list. */
        final TaskAdapter taskAdapter = new TaskAdapter(getActivity(), taskList);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(taskAdapter);

        //Find FAB for adding a task
        FloatingActionButton addTaskFab = rootView.findViewById(R.id.add_task_fab);

//        //Find FAB to remove all tasks
//        FloatingActionButton removeTasksFab = rootView.findViewById(R.id.remove_tasks_fab);

        //Sets an onClickListener on each item of the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task currentTask = taskList.get(i);
                //Since the user tapped on an item, its status is changed.
                currentTask.changeTaskStatus();
                //Updates the Database to reflect the change just made
                db.TaskDao().updateTaskStatus(currentTask.getTaskStatus(), currentTask.getID());
                //Notifies the adapter that the dataset has changed and refreshes it
                taskAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Shows the user an Alert before deleting an item from the list
                showAlertOnDeletion(view, db, taskAdapter, i);
                return true;
            }
        });

        //Sets a click Listener on the RemoveTaskFab and calls showAlertNukeButtonClicked()
//        removeTasksFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Shows the user an Alert before deleting all items from the list
//                showAlertNukeButtonClicked(view, db, taskAdapter);
//            }
//        });
//
//        addTaskFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addTaskIntent();
//                getActivity().finish();
//            }
//        });

        return rootView;
    }



    /**
     * Function that starts another activity to get the name of the Task
     */
    private void addTaskIntent() {
        startActivity(new Intent(getActivity(), AddTaskActivity.class));
    }


    /**
     * Requests a confirmation from the user before deleting everything
     *
     * @param view view?
     * @param db          is the database where all entries are stored
     * @param taskAdapter needed to update the listview on screen
     */
    public void showAlertNukeButtonClicked(View view, final AppDatabase db, final TaskAdapter taskAdapter) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                //Notifies the adapter that dataset has changed so it can refresh
                taskAdapter.notifyDataSetChanged();
            }
        });
        //Negative button does nothing
        builder.setNegativeButton(getString(R.string.cancel), null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * @param view view?
     * @param db is the database with all items
     * @param taskAdapter is the adapter which takes care of displaying all tasks
     * @param position  is the position in which the adapter is currently
     */
    public void showAlertOnDeletion(View view, final AppDatabase db,
                                    final TaskAdapter taskAdapter, final int position) {

        //Retrieve currentTask from adapter
        final Task currentTask = taskList.get(position);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title and message for the warning
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.deleteConfirmation) + currentTask.getTaskName() + " ?");
        // add the buttons
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //remove current entry from the database and from the List
                db.TaskDao().deleteTaskById(currentTask.getID());
                taskList.remove(position);
                taskAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
