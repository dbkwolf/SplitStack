package com.example.splitstack.DBUtility

import java.io.Serializable

data class UserData (

    val eventList:ArrayList<String>? = null,
    val firstName: String? = "",
    val lastName: String? = "",
    val totalDebt: String? = "",
    val totalCredit: String? =""
):Serializable{
}