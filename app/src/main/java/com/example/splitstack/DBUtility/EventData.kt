package com.example.splitstack.DBUtility

data class EventData (


    val eventName : String? = "",
    var isActive : Boolean? = true,
    val participants: ArrayList<String>? = null,
    val totalExpenses:String? = ""


)
{
    var id: String? = ""

}