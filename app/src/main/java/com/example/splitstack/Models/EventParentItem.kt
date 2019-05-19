package com.example.splitstack.Models

import com.bignerdranch.expandablerecyclerview.Model.ParentObject
import java.util.UUID

class EventParentItem(var title: String?) : ParentObject {

    private var mChildrenList: List<Any>? = null

    var id: UUID? = null

    init {
        id = UUID.randomUUID()
    }

    override fun getChildObjectList(): List<Any>? {
        return mChildrenList
    }

    override fun setChildObjectList(list: List<Any>) {
        mChildrenList = list
    }
}
