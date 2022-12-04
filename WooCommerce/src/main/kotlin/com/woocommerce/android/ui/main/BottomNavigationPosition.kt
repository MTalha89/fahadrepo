package com.woocommerce.android.ui.main

import com.woocommerce.android.R

const val PRODUCTS_POSITION = 0
const val ANALYTICS_POSITION = 1
const val ORDERS_POSITION = 2
const val MORE_POSITION = 3

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    PRODUCTS(PRODUCTS_POSITION, R.id.products),
    ANALYTICS(ANALYTICS_POSITION, R.id.analytics),
    ORDERS(ORDERS_POSITION, R.id.orders),
    MORE(MORE_POSITION, R.id.moreMenu)
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
     BottomNavigationPosition.PRODUCTS.id -> BottomNavigationPosition.PRODUCTS
    BottomNavigationPosition.ANALYTICS.id -> BottomNavigationPosition.ANALYTICS
    BottomNavigationPosition.ORDERS.id -> BottomNavigationPosition.ORDERS
    BottomNavigationPosition.MORE.id -> BottomNavigationPosition.MORE
    else -> BottomNavigationPosition.PRODUCTS
}

fun BottomNavigationPosition.getTag(): String = id.toString()
