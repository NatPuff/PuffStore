package com.example.puffstore.ViewModel

import com.example.puffstore.Model.CartModel

interface CartLoadListener {

    fun onLoadCart(cartModelList: List<CartModel>)
    fun onLoadCartFailed(message: String?)



}