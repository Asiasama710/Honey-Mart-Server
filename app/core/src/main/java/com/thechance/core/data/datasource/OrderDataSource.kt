package com.thechance.core.data.datasource

import com.thechance.core.data.model.OrderItem
import com.thechance.core.data.model.OrderWithPrice

interface OrderDataSource {
    suspend fun createOrder(
        totalPrice: Double,
        products: List<OrderItem>,
        userId:Long
    ): Boolean

    suspend fun getAllOrdersForMarket(
        marketId: Long
    ): List<OrderWithPrice>

    suspend fun cancelOrder(
        orderId:Long
    )
    //end region
}