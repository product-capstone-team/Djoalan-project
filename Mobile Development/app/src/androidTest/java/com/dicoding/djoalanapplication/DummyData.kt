package com.dicoding.djoalanapplication

import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.data.transaction.Transaction
import com.dicoding.djoalanapplication.data.user.User

object DummyData {

    fun generateUserEntities(): User {
        return User(
            userId = "abc1",
            name = "imran",
            email = "imran@mail.com",
            password = "sabaraha",
            isBusinessAccount = false
        )
    }

    fun generateProductEntities(): List<Product> {
        val listOfProducts = ArrayList<Product>()

        for (i in 1..5) {
            val newProduct = Product(
                productId = "P1",
                productName = "chitato roasted beef 200mg",
                brand = "chitato",
                expireDate = "2020-10-04",
                price = 10000,
                category = "food",
                tId = 1
            )
            listOfProducts.add(newProduct)
        }
        return listOfProducts
    }

    fun generateTransactionEntities(): Transaction {
       return Transaction(
           1,
           100000,
           "2020-12-08"
       )
    }


}