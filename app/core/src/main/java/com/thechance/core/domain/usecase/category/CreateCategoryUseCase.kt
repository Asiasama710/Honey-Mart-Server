package com.thechance.core.domain.usecase.category

import com.thechance.core.domain.repository.HoneyMartRepository
import com.thechance.core.entity.Category
import com.thechance.core.utils.*
import org.koin.core.component.KoinComponent
import java.util.regex.Pattern

class CreateCategoryUseCase(private val repository: HoneyMartRepository) : KoinComponent {
    suspend operator fun invoke(categoryName: String?, marketId: Long?, imageId: Int?): Category {

        isValidInput(categoryName, marketId, imageId)?.let { throw it }

        val isMarketDeleted = repository.isMarketDeleted(marketId!!)
        return if (isMarketDeleted == null) {
            throw IdNotFoundException()
        } else if (isMarketDeleted) {
            throw MarketDeletedException()
        } else {
            if (repository.isCategoryNameUnique(categoryName!!)) {
                repository.createCategory(categoryName, marketId, imageId!!)
            } else {
                throw CategoryNameNotUniqueException()
            }
        }
    }

    private fun isValidInput(categoryName: String?, marketId: Long?, imageId: Int?): Exception? {
        return when {
            !isValidCategoryName(categoryName) -> {
                InvalidCategoryNameException()
            }

            isInvalidId(marketId) -> {
                InvalidMarketIdException()
            }

            isInvalidId(imageId?.toLong()) -> {
                InvalidImageIdException()
            }

            else -> {
                null
            }
        }
    }

    private fun isValidCategoryName(categoryName: String?): Boolean {
        return if (categoryName == null) {
            return false
        } else {
            val categoryNameRegex = Regex("^[a-zA-Z_]\\w{3,9}$")
            categoryNameRegex.matches(categoryName)
        }
    }
}
