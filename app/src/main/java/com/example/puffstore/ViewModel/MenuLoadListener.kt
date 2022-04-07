package com.example.puffstore.ViewModel

import com.example.puffstore.Model.MenuModel

interface MenuLoadListener {
    fun onMenuLoad(menuModelList: List<MenuModel>?)

    fun onMenuLoadFailed(message: String?)

}