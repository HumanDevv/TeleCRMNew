package com.tele.crm.domain.entites

data class MetaItem(
    val id: String,
    var title: String
){
    override fun toString(): String {
        return title
    }
}
