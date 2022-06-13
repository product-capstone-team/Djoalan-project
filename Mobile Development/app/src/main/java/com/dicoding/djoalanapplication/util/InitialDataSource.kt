package com.dicoding.djoalanapplication.util

import com.dicoding.djoalanapplication.data.product.Product

object InitialDataSource {
    fun getDummyProducts(): List<Product> {
        return listOf(
            Product(
                productId = "A01",
                productName = "chitato Roasted Beef 200MG",
                brand = "chitato",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 24500,
                category = "food",
                quantity = 1,
                imageUrl = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2021/3/3/59aa3f8c-2ee5-4134-801e-902076365594.jpg"
            ),
            Product(
                productId = "A02",
                productName = "pocari sweat botol 2L",
                brand = "pocari",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 31800,
                category = "drink",
                quantity = 1,
                imageUrl = "https://www.heymart.id/wp-content/uploads/2020/07/POCARI-SWEAT-Ion-Supply-Drink-2-liter-1-Botol.jpg"

            ),
            Product(
                productId = "A03",
                productName = "doritos nacho cheese",
                brand = "doritos",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 13400,
                category = "food",
                quantity = 1,
                imageUrl = "https://www.ubuy.co.id/productimg/?image=aHR0cHM6Ly9tLm1lZGlhLWFtYXpvbi5jb20vaW1hZ2VzL0kvNzFNUWVJUzdGQUwuX1NMMTUwMF8uanBn.jpg"
            ),
            Product(
                productId = "A04",
                productName = "snickers chocolate bar",
                brand = "snickers",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 24500,
                category = "food",
                quantity = 1,
                imageUrl = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2021/6/21/edd43416-1b99-4cb7-8d03-2646b6c76b09.jpg"
            ),
            Product(
                productId = "A05",
                productName = "M&M chocolate bar",
                brand = "M&M",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 89000,
                category = "food",
                quantity = 1,
                imageUrl = "https://s4.bukalapak.com/img/93908368672/large/data..webp"
            ),
            Product(
                productId = "A06",
                productName = "cadbury chocolate bar",
                brand = "cadbury",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 33500,
                category = "food",
                quantity = 1,
                imageUrl = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2020/6/26/6c7784f6-22cc-4e55-8e8a-33b10e58f554.png"
            ),
            Product(
                productId = "A07",
                productName = "sprite lemon can",
                brand = "sprite",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 12500,
                category = "drink",
                quantity = 1,
                imageUrl = "https://5.imimg.com/data5/UE/NE/WQ/SELLER-82456434/sprinte-cold-derink-500x500.jpg"
            ),
            Product(
                productId = "A08",
                productName = "coca cola zero",
                brand = "coca cola",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 11000,
                category = "drink",
                quantity = 1,
                imageUrl = "https://images.unsplash.com/photo-1630404365865-97ff92feba6a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8c29kYSUyMGNhbnxlbnwwfHwwfHw%3D&w=1000&q=80"
            ),
            Product(
                productId = "A09",
                productName = "redbull energy drink",
                brand = "redbull",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 24500,
                category = "drink",
                quantity = 1,
                imageUrl = "https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//112/MTA-25568788/red-bull_red-bull-energy-drink-can-250-ml_full01.jpg"
            ),
            Product(
                productId = "A001",
                productName = "coca cola original taste 330ml",
                brand = "coca cola",
                expireDate = "2022-03-28T05:18:27 -07:00",
                price = 17800,
                category = "drink",
                quantity = 1,
                imageUrl = "https://cdn3.evostore.io/productimages/vow_premium/l/au00099.jpg"
            ),
        )
    }
}