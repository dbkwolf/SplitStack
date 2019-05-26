package com.example.splitstack.SQLite

import android.provider.BaseColumns

object UserHistoryContract {

    object HistoryEntry: BaseColumns {
        const val TABLE_NAME = "history"

        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_ACTIVITY = "activity"
        const val COLUMN_NAME_USER = "user"





    }
}