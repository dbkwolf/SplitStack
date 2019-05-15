package com.example.splitstack.DBUtility

data class EventData (

    val eventName : String? = "",
    val isActive : Boolean = true,
    val participants: HashMap<String, Any>? = null,
    val totalExpenses:String? = ""


)
{
}