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
public abstract class DateDialog extends DialogFragment implements OnDateSetListener {

    public static final Integer DATE_VALIDARION_ON = 99;
    public static final Integer DATE_VALIDARION_OFF = -99;

    Context context;
    Calendar calendar;
    Integer year;
    Integer month;
    Integer day;
    SimpleDateFormat dateFormat;
    Integer validation;

    public DateDialog(Context context, Integer validation) {
        this.context = context;
        this.validation = validation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        try {
            if (this.validation == DATE_VALIDARION_ON) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, day);
                
                if (selectedCalendar.compareTo(calendar) == -1) {
                    Toast.makeText(context, "Tidak bisa memilih hari kemarin", Toast.LENGTH_SHORT).show();
                } else {
                    String dateString = day + "/" + (month + 1) + "/" + year;
                    onDateSelected(dateFormat.parse(dateString));
                }
            } else {
                String dateString = day + "/" + (month + 1) + "/" + year;
                onDateSelected(dateFormat.parse(dateString));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    protected abstract void onDateSelected(Date date);

    protected abstract void onError(String message);
}
