package com.thechance.api.endpoints

import com.thechance.api.ServerResponse
import com.thechance.api.model.mapper.toApiOrderModel
import com.thechance.api.model.mapper.toApiOrders
import com.thechance.core.domain.usecase.order.OrderUseCasesContainer
import com.thechance.core.utils.ROLE_TYPE
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoutes() {

    val orderUseCasesContainer: OrderUseCasesContainer by inject()

    authenticate {
        route("/order") {

            get("/marketOrders") {
                val principal = call.principal<JWTPrincipal>()
                val ownerId = principal?.payload?.subject?.toLongOrNull()
                val role = principal?.getClaim(ROLE_TYPE, String::class)

                val orders =
                    orderUseCasesContainer.getOrdersForMarketUseCase(ownerId, role).map { it.toApiOrderModel() }
                call.respond(ServerResponse.success(orders))

            }

            get("/userOrders") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject?.toLongOrNull()
                val role = principal?.getClaim(ROLE_TYPE, String::class)
                val orders =
                    orderUseCasesContainer.getOrdersForUserUseCase(userId, role).toApiOrders()
                call.respond(ServerResponse.success(orders))

            }

            /**
             * get Order Details
             * */
            get("/{id}") {
                val orderId = call.parameters["id"]?.trim()?.toLongOrNull()
                val orders =
                    orderUseCasesContainer.getOrderDetailsUseCase(orderId).toApiOrderModel()
                call.respond(ServerResponse.success(orders))

            }

            post("/checkout") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.subject?.toLongOrNull()
                val role = principal?.getClaim(ROLE_TYPE, String::class)

                val isCreated = orderUseCasesContainer.createOrderUseCase(userId, role)
                call.respond(ServerResponse.success(isCreated,"Checkout Success"))

            }

            /**
             * Update order state
             */
            put("/{id}") {
                val params = call.receiveParameters()
                val orderId = call.parameters["id"]?.trim()?.toLongOrNull()
                val orderState = params["state"]?.toIntOrNull()

                val principal = call.principal<JWTPrincipal>()
                val role = principal?.getClaim(ROLE_TYPE, String::class)

                val updatedStatus = orderUseCasesContainer.updateOrderStateUseCase(orderId, orderState, role)
                call.respond(ServerResponse.success(updatedStatus))

            }
        }
    }
}