package com.example.echoes.utils

import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimePickerUtils {
    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate =
                    SimpleDateFormat("dd MMM yyyy", Locale.US).format(calendar.time)
                onDateSelected(formattedDate)
            },
            year, month, 1
        )
        datePickerDialog.show()
    }

    fun showTimePickerDialog(
        context: Context,
        initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
        is24HourView: Boolean = true,
        onTimeSelected: (hour: Int, minute: Int) -> Unit
    ) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                onTimeSelected(hour, minute)
            },
            initialHour,
            initialMinute,
            is24HourView
        ).show()
    }
}