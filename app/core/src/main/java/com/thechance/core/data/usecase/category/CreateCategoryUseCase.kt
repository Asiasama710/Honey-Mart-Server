package com.thechance.core.data.usecase.category

import com.thechance.core.data.model.Category
import com.thechance.core.data.repository.HoneyMartRepository
import com.thechance.core.data.utils.*
import org.koin.core.component.KoinComponent
import java.util.regex.Pattern

class CreateCategoryUseCase(private val repository: HoneyMartRepository) : KoinComponent {
    suspend operator fun invoke(categoryName: String?, marketId: Long?, imageId: Int?): Category {

        isValidInput(categoryName, marketId, imageId)?.let { throw it }

        return if (!repository.isMarketDeleted(marketId!!)) {
            if (repository.isCategoryNameUnique(categoryName!!)) {
                repository.createCategory(categoryName, marketId, imageId!!)
            } else {
                throw CategoryNameNotUniqueException()
            }
        } else {
            throw MarketDeletedException()
        }
    }

    private fun isValidInput(categoryName: String?, marketId: Long?, imageId: Int?): Exception? {
        return when {
            checkName(categoryName) -> {
                InvalidCategoryNameException()
            }

            checkLetter(categoryName) -> {
                InvalidCategoryNameLettersException()
            }

            isValidId(marketId) -> {
                InvalidMarketIdException()
            }

            isValidId(imageId?.toLong()) -> {
                InvalidImageIdException()
            }

            else -> {
                null
            }
        }
    }

    private fun checkLetter(categoryName: String?): Boolean {
        return categoryName?.let {
            return !isValidStringInput(it)
        } ?: true
    }

    private fun isValidStringInput(name: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z\\s]+$")
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }
}
