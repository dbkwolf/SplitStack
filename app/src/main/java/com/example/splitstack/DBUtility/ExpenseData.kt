package com.example.splitstack.DBUtility

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.Date

class ExpenseData(

    val expenseId: String? = "",
    val amount: String? ="",
    @ServerTimestamp
    var timestamp: Date? = null

) {}