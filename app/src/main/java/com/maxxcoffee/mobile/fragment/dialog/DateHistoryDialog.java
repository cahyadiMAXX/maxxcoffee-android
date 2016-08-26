package com.maxxcoffee.mobile.fragment.dialog;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public abstract class DateHistoryDialog extends DialogFragment implements OnDateSetListener {

    Context context;
    Calendar calendar;
    Integer year;
    Integer month;
    Integer day;
    SimpleDateFormat dateFormat;

    public DateHistoryDialog(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) - 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        try {
            String dateString = day + "/" + (month + 1) + "/" + year;
            onDateSelected(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    protected abstract void onDateSelected(Date date);

    protected abstract void onError(String message);
}
