package com.thechance.api.endpoints

import com.thechance.api.ServerResponse
import com.thechance.api.mapper.toApiCart
import com.thechance.core.domain.usecase.cart.CartUseCasesContainer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.cartRoutes() {

    val cartUseCasesContainer: CartUseCasesContainer by inject()

    authenticate {
        route("/cart") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", Long::class)

                val products = cartUseCasesContainer.getCartUseCase(userId).toApiCart()
                call.respond(ServerResponse.success(products))

            }

            post("/addProduct") {

                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", Long::class)

                val params = call.receiveParameters()
                val productId = params["productId"]?.trim()?.toLongOrNull()
                val count = params["count"]?.trim()?.toIntOrNull()
                cartUseCasesContainer.addProductToCartUseCase(userId = userId, productId = productId, count)
                call.respond(HttpStatusCode.Created, ServerResponse.success("Added successfully"))

            }

            delete {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", Long::class)

                val params = call.receiveParameters()
                val productId = params["productId"]?.trim()?.toLongOrNull()
                cartUseCasesContainer.deleteProductInCartUseCase(userId = userId, productId = productId)
                call.respond(HttpStatusCode.OK, ServerResponse.success("Deleted successfully"))

            }
        }
    }
}