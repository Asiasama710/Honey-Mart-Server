package com.thechance.core.data.repository

import com.thechance.core.entity.*
import com.thechance.core.data.repository.dataSource.OwnerDataSource
import com.thechance.core.data.repository.dataSource.UserDataSource
import com.thechance.core.data.repository.security.HashingService
import com.thechance.core.data.security.hashing.SaltedHash
import com.thechance.core.data.security.token.TokenClaim
import com.thechance.core.data.security.token.TokenConfig
import com.thechance.core.data.repository.security.TokenService
import com.thechance.core.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent

class AuthRepositoryImp(
    private val userDataSource: UserDataSource,
    private val ownerDataSource: OwnerDataSource,
    private val hashingService: HashingService,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig
) : AuthRepository, KoinComponent {

    //region user
    override suspend fun createUser(userName: String, password: String, fullName: String, email: String): Boolean {
        val saltedHash = hashingService.generateSaltedHash(password)
        return userDataSource.createUser(userName, saltedHash, fullName, email)
    }

    override suspend fun isUserNameExists(userName: String): Boolean =
        userDataSource.isUserNameExists(userName)

    override suspend fun isEmailExists(email: String): Boolean = userDataSource.isEmailExists(email)

    override suspend fun isUserExist(userId: Long): Boolean = userDataSource.isUserExist(userId)


    override suspend fun getUserByName(userName: String): User {
        return userDataSource.getUserByName(userName)
    }

    override fun getToken(id: Long, role: String): String {
        return tokenService.generate(
            config = tokenConfig,
            subject = id.toString(),
            TokenClaim(name = "role", value = role)
        )
    }

    override fun isValidPassword(user: User, password: String) = hashingService.verify(
        value = password,
        saltedHash = SaltedHash(hash = user.password, salt = user.salt)
    )


    //endregion

    //region owner
    override suspend fun createOwner(userName: String, password: String): Owner =
        ownerDataSource.createOwner(userName, password)

    override suspend fun isOwnerNameExists(ownerName: String): Boolean =
        ownerDataSource.isOwnerNameExists(ownerName)

    //endregion

}