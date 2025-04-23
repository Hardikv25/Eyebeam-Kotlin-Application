package com.example.Eyebeam.Helper

import android.content.Context
import android.widget.Toast
import com.example.Eyebeam.Model.ItemsModel


class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItems(item: ItemsModel) {
        var listCloth = getListCart()
        val existAlready = listCloth.any { it.title == item.title }
        val index = listCloth.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listCloth[index].numberInCart = item.numberInCart
        } else {
            listCloth.add(item)
        }
        tinyDB.putListObject("CartList", listCloth)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }
    fun clearCart() {
        tinyDB.putListObject("CartList", arrayListOf<ItemsModel>()) // Clear the cart
//        Toast.makeText(context, "Cart cleared!", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): java.util.ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listItems[position].numberInCart++
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listCloth = getListCart()
        var fee = 0.0
        for (item in listCloth) {
            fee += item.price * item.numberInCart
        }
        return fee
    }
}