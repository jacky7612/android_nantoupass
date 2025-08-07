package com.jotangi.nantoupass.utility

import java.text.SimpleDateFormat
import java.util.*

object DateUtility {

    const val FILTER_WEEK = "week"
    const val FILTER_MONTH = "month"
    const val FILTER_SEASON = "season"
    private const val dayTimeStamp: Long = 86400000
    private var weekTimeStamp: Long = dayTimeStamp * 7
    private var monthTimeStamp: Long = weekTimeStamp * 4
    private var seasonTimeStamp: Long = monthTimeStamp * 3
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private val calendar = Calendar.getInstance()

    fun isExpired(
        filterDay: String,
        filterType: String
    ): Boolean {
        val previousDay = when (filterType) {
            FILTER_WEEK -> {
                val today = Date().time
                val filterTime = kotlin.math.abs(weekTimeStamp)
                sdf.format(today - filterTime)
            }

            FILTER_MONTH -> {
                val mYear = calendar.get(Calendar.YEAR)
                val mPreMonth = when (calendar.get(Calendar.MONTH) - 1 == 0) {
                    true -> 12
                    else -> calendar.get(Calendar.MONTH)
                }
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val mDate = "$mYear-$mPreMonth-$mDay"
                val mDateParse = sdf.parse(mDate).time
                sdf.format(mDateParse)
            }

            FILTER_SEASON -> {
                val mYear = calendar.get(Calendar.YEAR)
                val mPreMonth = when (calendar.get(Calendar.MONTH) + 1 - 3) {
                    0 -> 12
                    -1 -> 11
                    -2 -> 10
                    else -> calendar.get(Calendar.MONTH) + 1 - 3
                }
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val mDate = "$mYear-$mPreMonth-$mDay"
                val mDateParse = sdf.parse(mDate).time
                sdf.format(mDateParse)
            }

            else -> {
                sdf.format(Date())
            }
        }
        val previousDate: Date = sdf.parse(previousDay)!!

        // data date
        val filterDate: Date = sdf.parse(filterDay)!!

        if (previousDate > filterDate) {
            return true
        }

        return false
    }

    fun getToday(): String {
        return sdf.format(Date().time)
    }

    fun getPreviousDay(filterType: String): String {
        return when (filterType) {
            FILTER_WEEK -> {
                val today = Date().time
                val filterTime = kotlin.math.abs(weekTimeStamp)
                sdf.format(today - filterTime)
            }

            FILTER_MONTH -> {
                val mYear = calendar.get(Calendar.YEAR)
                val mPreMonth = when (calendar.get(Calendar.MONTH) - 1 == 0) {
                    true -> 12
                    else -> calendar.get(Calendar.MONTH)
                }
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val mDate = "$mYear-$mPreMonth-$mDay"
                val mDateParse = sdf.parse(mDate).time
                sdf.format(mDateParse)
            }

            FILTER_SEASON -> {
                val mYear = calendar.get(Calendar.YEAR)
                val mPreMonth = when (calendar.get(Calendar.MONTH) + 1 - 3) {
                    0 -> 12
                    -1 -> 11
                    -2 -> 10
                    else -> calendar.get(Calendar.MONTH) + 1 - 3
                }
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val mDate = "$mYear-$mPreMonth-$mDay"
                val mDateParse = sdf.parse(mDate).time
                sdf.format(mDateParse)
            }

            else -> {
                sdf.format(Date())
            }
        }
    }
}