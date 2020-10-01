package com.himdeve.tmdb.interview.presentation.movies.views

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.himdeve.tmdb.interview.R
import timber.log.Timber
import java.util.*

/**
 * Created by Himdeve on 10/1/2020.
 */

const val MAX_DAYS = 14
const val MAX_DAYS_ERROR = "Max day is 14"

// TODO: better to use fragment dialog instead to prevent leaked window.
//  Or launch this dialog from Coroutine scope (Current solution)
class MovieDateDialog(
    private val context: Context,
    private var position: Int = 0,
    private val onPositiveClick: (startDate: Calendar?, endDate: Calendar?, position: Int) -> Unit,
    private val onNegativeClick: () -> Unit,
) {
    private val _alertDialog: AlertDialog
    private val _dayList = mutableListOf<String>()

    init {
        val spinner = Spinner(context)

        prepareDayList()

        val arrayAdapter =
            ArrayAdapter(context, R.layout.simple_spinner_item, _dayList)
        spinner.adapter = arrayAdapter

        if (position > MAX_DAYS) {
            // TODO: inform user or change position type to restrict max days of MAX_DAYS
            Timber.e(MAX_DAYS_ERROR)
            position = 0
        }
        spinner.setSelection(position)

        _alertDialog = prepareAlertDialogBuilder(spinner).create()
    }

    fun show() {
        _alertDialog.show()
    }

    fun dismiss() {
        _alertDialog.dismiss()
    }

    private fun prepareDayList() {
        // The first value is without any restriction
        _dayList.add(context.getString(R.string.settings_last_days_no_restriction))

        // added suffix (e.g. [1] day back or [5] days back)
        val day = context.getString(R.string.settings_last_days_suffix_day)
        val days = context.getString(R.string.settings_last_days_suffix_days)

        // added first value as singular "day"
        _dayList.add("1 $day")

        // from 2 because of suffix "days"
        (2..MAX_DAYS).forEach { i ->
            _dayList.add("$i $days")
        }
    }

    private fun prepareAlertDialogBuilder(spinner: Spinner): AlertDialog.Builder {
        val retAlertDialogBuilder = AlertDialog.Builder(context)

        retAlertDialogBuilder.setTitle(R.string.settings_last_days_title)
        retAlertDialogBuilder.setMessage(R.string.settings_last_days_desc)
        retAlertDialogBuilder.setView(spinner)

        // Set up positive button
        retAlertDialogBuilder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            val day = spinner.selectedItemPosition

            if (day <= 0) {
                // no restriction
                onPositiveClick(null, null, 0)
            } else {
                val startDate = Calendar.getInstance().apply {
                    // start date is current date minus selected days
                    add(Calendar.DATE, -day)
                }

                val endDate: Calendar = Calendar.getInstance().apply {
                    // end date is start date plus 1 day
                    add(Calendar.DATE, -day + 1)
                }

                onPositiveClick(startDate, endDate, day)
            }

            dialog.dismiss()
        }

        // Set up negative button
        retAlertDialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            onNegativeClick()
            dialog.dismiss()
        }

        return retAlertDialogBuilder
    }
}