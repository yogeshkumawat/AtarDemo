package com.atar.demo.model

data class ListItem(val ID: Int,
               val Title: String,
               val Description: String,
               val PageCount: Int,
               val PublishDate: String
) : BaseItem(1)