package com.thechance.core.data.usecase.category

import com.thechance.core.data.model.Category
import com.thechance.core.data.service.CategoryService
import com.thechance.core.data.utils.*
import java.util.regex.Pattern

class CreateCategoryUseCase(private val categoryService: CategoryService) {
    suspend operator fun invoke(categoryName: String?, marketId: Long?, imageId: Int?): Category {
        return if (checkCategoryName(categoryName)) {
            throw InvalidCategoryNameException()
        } else if (checkLetter(categoryName)) {
            throw InvalidCategoryNameLettersException()
        } else if (checkMarketId(marketId)) {
            throw InvalidMarketIdException()
        } else if (checkImageId(imageId)) {
            throw InvalidImageIdException()
        } else {
            categoryService.create(categoryName, marketId, imageId)
        }
    }

    private fun checkCategoryName(categoryName: String?): Boolean {
        return categoryName?.let {
            return it.length !in 6..20
        } ?: true
    }

    private fun checkLetter(categoryName: String?): Boolean {
        return categoryName?.let {
            return !isValidStringInput(it)
        } ?: true
    }

    private fun checkMarketId(marketId: Long?): Boolean {
        return marketId == null
    }

    private fun checkImageId(imageId: Int?): Boolean {
        return imageId == null
    }

    private fun isValidStringInput(name: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z\\s]+$")
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }
}
