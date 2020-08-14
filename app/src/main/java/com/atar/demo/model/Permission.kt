package com.atar.demo.model

data class Permission(val admin: Boolean, val push: Boolean, val pull: Boolean) {
    override fun toString(): String {
        return "Permission admin[$admin], push[$push], pull[$pull]"
    }
}