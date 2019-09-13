package com.example.android.taskdo;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TuesdayFragment extends Fragment {
    private static final String TAG = "MondayFragment";

    private static final int DAY = 2;


    private List<Task> taskList;

    private TaskAdapter taskAdapter;


    public TuesdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.task_list, container, false);

        //DATABASE
        final AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "database")
                .allowMainThreadQueries().build();

        //Loads the tasks from the database
        taskList = db.TaskDao().getTasksByDay(DAY);


        /*
         * Create a {@link TaskAdapter}, whose data source is a list of
         Tasks. The adapter knows how to create list item views for each item
         in the list. */
        taskAdapter = new TaskAdapter(getActivity(), taskList);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(taskAdapter);

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
                showAlertOnDeletion(db, taskAdapter, i);
                return true;
            }
        });

        return rootView;
    }

    /**
     * @param db          is the database with all items
     * @param taskAdapter is the adapter which takes care of displaying all tasks
     * @param position    is the position in which the adapter is currently
     */
    public void showAlertOnDeletion(final AppDatabase db,
                                    final TaskAdapter taskAdapter, final int position) {

        //Retrieve currentTask from adapter
        final Task currentTask = taskList.get(position);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title and message for the warning
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.deleteConfirmation) + currentTask.getName() + " ?");
        // add the buttons
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //remove current entry from the database and from the List
                db.TaskDao().deleteTasksById(currentTask.getID());
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

