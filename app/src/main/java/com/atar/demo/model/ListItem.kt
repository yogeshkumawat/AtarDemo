package com.atar.demo.model

data class ListItem(val name: String,
               val description: String,
               val open_issues_count: Int,
               val license: Licence,
               val permissions: Permission
) : BaseItem(1)