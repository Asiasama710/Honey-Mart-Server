package com.thechance.core.domain.usecase.admin

import com.thechance.core.domain.repository.AuthRepository
import com.thechance.core.entity.market.Market
import com.thechance.core.utils.ADMIN_ROLE
import com.thechance.core.utils.AdminAccessDeniedException
import com.thechance.core.utils.isValidRole
import org.koin.core.component.KoinComponent

class GetUnApprovedMarkets(private val repository: AuthRepository) : KoinComponent {

    suspend operator fun invoke(role:String?): List<Market> {
        if (!isValidRole(ADMIN_ROLE, role)) {
            throw AdminAccessDeniedException()
        }
        return repository.getUnApprovedMarkets()
    }

}