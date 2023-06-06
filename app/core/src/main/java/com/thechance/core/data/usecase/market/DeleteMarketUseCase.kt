package com.thechance.core.data.usecase.market

import com.thechance.core.data.repository.HoneyMartRepository
import com.thechance.core.data.utils.IdNotFoundException
import com.thechance.core.data.utils.InvalidMarketIdException
import com.thechance.core.data.utils.MarketDeletedException
import com.thechance.core.data.utils.isInvalidId
import org.koin.core.component.KoinComponent

class DeleteMarketUseCase(private val repository: HoneyMartRepository) : KoinComponent {
    suspend operator fun invoke(marketId: Long?): Boolean {
        return if (isInvalidId(marketId)) {
            throw InvalidMarketIdException()
        } else {
            val isDeleted = repository.isMarketDeleted(marketId!!)
            if (isDeleted == null) {
                throw IdNotFoundException()
            } else if (isDeleted) {
                throw MarketDeletedException()
            } else {
                repository.deleteMarket(marketId)
            }
        }
    }
}