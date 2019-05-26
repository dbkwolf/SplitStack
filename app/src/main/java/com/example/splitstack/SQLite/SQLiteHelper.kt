package com.example.splitstack.SQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {

        val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${UserHistoryContract.HistoryEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${UserHistoryContract.HistoryEntry.COLUMN_NAME_DATE} TEXT," +
                    "${UserHistoryContract.HistoryEntry.COLUMN_NAME_ACTIVITY} TEXT," +
                    "${UserHistoryContract.HistoryEntry.COLUMN_NAME_USER} TEXT)"


        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UserHistoryContract.HistoryEntry.TABLE_NAME}"

        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "SplitStackHistory.db"
    }
}