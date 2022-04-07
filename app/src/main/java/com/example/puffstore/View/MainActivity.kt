package com.example.puffstore.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.puffstore.Model.CartModel
import com.example.puffstore.Model.MenuModel
import com.example.puffstore.R
import com.example.puffstore.ViewModel.CartLoadListener
import com.example.puffstore.ViewModel.MenuAdapter
import com.example.puffstore.ViewModel.MenuLoadListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MenuLoadListener, CartLoadListener {

    lateinit var menuLoadListener: MenuLoadListener
    lateinit var cartLoadListener: CartLoadListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        loadMenuFromFirebase()
        countCartFirebase()
    }

    private fun countCartFirebase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance().getReference("Cart").child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun loadMenuFromFirebase() {
        val menuModelsFirebase: MutableList<MenuModel> = ArrayList()
        FirebaseDatabase.getInstance().getReference("Menu").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(menuSnapshot in snapshot.children){
                        val menuModel = menuSnapshot.getValue(MenuModel::class.java)
                        menuModel!!.key = menuSnapshot.key
                        menuModelsFirebase.add(menuModel)
                    }
                    menuLoadListener.onMenuLoad(menuModelsFirebase)
                }
                else
                    menuLoadListener.onMenuLoadFailed("Item does not exist")
            }

            override fun onCancelled(error: DatabaseError) {
                menuLoadListener.onMenuLoadFailed(error.message)
            }

        })
    }

    private fun init(){
        menuLoadListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this, 2)
        menuRecycler.layoutManager = gridLayoutManager
        menuRecycler.addItemDecoration(ItemSpacing())


    }

    override fun onMenuLoad(menuModelList: List<MenuModel>?) {
        val adapter = MenuAdapter(this, menuModelList!!, cartLoadListener)
        menuRecycler.adapter = adapter
    }

    override fun onMenuLoadFailed(message: String?) {
        Snackbar.make(mainLayout, message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun onLoadCart(cartModelList: List<CartModel>) {
        var cartSum = 0

        for(cartModel in cartModelList!!){
            cartSum+= cartModel!!.quantity
            badge!!.setNumber(cartSum)
        }
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout, message!!, Snackbar.LENGTH_LONG).show()
    }

    
}

