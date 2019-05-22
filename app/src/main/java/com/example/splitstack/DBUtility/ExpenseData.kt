package com.example.splitstack.DBUtility

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


class ExpenseData(

    val eventId: String? = "",
    val title: String? = "",
    val amount: String? ="",
    val userId: String? ="",
    val uName: String? ="",
    @ServerTimestamp
    var timestamp: Date? = null

) {  var id: String? = ""}