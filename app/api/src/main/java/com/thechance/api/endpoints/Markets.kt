package com.thechance.api.endpoints

import com.thechance.api.ServerResponse
import com.thechance.api.model.mapper.toApiCategoryModel
import com.thechance.api.model.mapper.toApiMarketModel
import com.thechance.api.utils.handleException
import com.thechance.core.domain.usecase.market.MarketUseCaseContainer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.marketsRoutes() {

    val marketUseCaseContainer: MarketUseCaseContainer by inject()

    route("/markets") {

        get {
            val markets = marketUseCaseContainer.getMarketsUseCase().map { it.toApiMarketModel() }
            call.respond(HttpStatusCode.OK, ServerResponse.success(markets))
        }

        get("/{id}/categories") {
            handleException(call) {
                val marketId = call.parameters["id"]?.toLongOrNull()
                val categories =
                    marketUseCaseContainer.getCategoriesByMarketIdUseCase(marketId).map { it.toApiCategoryModel() }
                call.respond(HttpStatusCode.OK, ServerResponse.success(categories))
            }
        }


        post {
            val marketName = call.receiveParameters()["name"]?.trim().orEmpty()
            handleException(call) {
                val newMarket = marketUseCaseContainer.createMarketUseCase(marketName).toApiMarketModel()
                call.respond(
                    HttpStatusCode.Created,
                    ServerResponse.success(newMarket, "Market created successfully")
                )
            }
        }


        put("/{id}") {
            handleException(call) {
                val marketId = call.parameters["id"]?.toLongOrNull()
                val marketName = call.receiveParameters()["name"]?.trim()
                val updatedMarket = marketUseCaseContainer.updateMarketUseCase(marketId, marketName).toApiMarketModel()

                call.respond(
                    HttpStatusCode.OK,
                    ServerResponse.success(updatedMarket, "Market updated successfully")
                )
            }
        }

        delete("/{id}") {
            val marketId = call.parameters["id"]?.toLongOrNull()
            handleException(call) {
                marketUseCaseContainer.deleteMarketUseCase(marketId)
                call.respond(HttpStatusCode.OK, ServerResponse.success("Market Deleted Successfully"))
            }
        }
    }
}