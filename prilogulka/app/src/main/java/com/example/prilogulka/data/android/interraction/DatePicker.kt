package com.example.prilogulka.data.android.interraction


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.fragment.app.DialogFragment

import com.example.prilogulka.R
import com.example.prilogulka.data.Time
import com.google.android.material.textfield.TextInputLayout

import java.util.Calendar

class DatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // определяем текущую дату
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // создаем DatePickerDialog и возвращаем его
        val picker = DatePickerDialog(context!!, this, year, month, day)
        picker.setTitle(resources.getString(R.string.choose_date))

        return picker
    }
    //    @Override
    //    public void onStart() {
    //        super.onStart();
    //    }

    override fun onDateSet(datePicker: android.widget.DatePicker, year: Int,
                           month: Int, day: Int) {

        val tv = activity!!.findViewById<View>(R.id.birthday) as TextView
        val bdayTextInput = activity!!.findViewById<TextInputLayout>(R.id.birthday_text_input_layout)

        val m = if (month < 9) "0${month + 1}" else "${month + 1}"
        val d = if (day < 10) "0$day" else "$day"
        tv.text = "$d.$m.$year"
        if (!Time.isAdult(tv.text.toString())) {
            bdayTextInput.isErrorEnabled = true
            bdayTextInput.error = "Вы моложе 18 лет."
        } else {
            bdayTextInput.isErrorEnabled = false
        }

    }
}
