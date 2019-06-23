package com.example.prilogulka.data.android.interraction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Time;

import java.util.Calendar;

public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle(getResources().getString(R.string.choose_date));

        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.ready));

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {

        TextView tv = (TextView) getActivity().findViewById(R.id.birthday);
        TextInputLayout bdayTextInput = getActivity().findViewById(R.id.birthday_text_input_layout);

        String m = (month < 9) ? "0" + (month+1) : (month+1) + "";
        String d = (day < 10) ? "0" + day : day + "";
        tv.setText(d + "." + m + "." + year);
        if (!Time.isAdult(tv.getText().toString())) {
            bdayTextInput.setErrorEnabled(true);
            bdayTextInput.setError("Вы моложе 18 лет.");
        } else {
            bdayTextInput.setErrorEnabled(false);
        }

    }
}
