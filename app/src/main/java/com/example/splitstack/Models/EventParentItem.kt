package com.example.splitstack.Models

import com.bignerdranch.expandablerecyclerview.Model.ParentObject
import java.util.UUID

class EventParentItem(var title: String?, var eventId: String?) : ParentObject {

    private var mChildrenList: List<Any>? = null

    var uuid: UUID? = null

    init {
        uuid = UUID.randomUUID()
    }

    override fun getChildObjectList(): List<Any>? {
        return mChildrenList
    }

    override fun setChildObjectList(list: List<Any>) {
        mChildrenList = list
    }
}
