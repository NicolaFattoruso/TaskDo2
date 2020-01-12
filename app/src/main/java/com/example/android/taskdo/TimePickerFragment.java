package com.example.android.taskdo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    private static final String TAG = "TimePickerFragment";


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        boolean is24HourView = DateFormat.is24HourFormat(getContext());


        return new TimePickerDialog(getContext(),
                (TimePickerDialog.OnTimeSetListener) getActivity(),
                hour, minute, is24HourView);
    }
}
