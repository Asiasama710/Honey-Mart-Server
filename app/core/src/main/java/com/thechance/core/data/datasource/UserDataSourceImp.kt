package com.thechance.core.data.datasource

import com.thechance.core.data.datasource.database.tables.NormalUserTable
import com.thechance.core.data.datasource.database.tables.ProductTable
import com.thechance.core.data.datasource.database.tables.cart.CartProductTable
import com.thechance.core.data.datasource.database.tables.cart.CartTable
import com.thechance.core.data.datasource.database.tables.wishlist.WishListProductTable
import com.thechance.core.data.datasource.database.tables.wishlist.WishListTable
import com.thechance.core.data.datasource.mapper.toProduct
import com.thechance.core.entity.*
import com.thechance.core.data.repository.dataSource.UserDataSource
import com.thechance.core.data.security.hashing.SaltedHash
import com.thechance.core.utils.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.koin.core.component.KoinComponent

class UserDataSourceImp : UserDataSource, KoinComponent {
    //region user
    override suspend fun createUser(
        userName: String, saltedHash: SaltedHash, fullName: String, email: String
    ): Boolean {
        return dbQuery {
            NormalUserTable.insert {
                it[NormalUserTable.userName] = userName
                it[password] = saltedHash.hash
                it[salt] = saltedHash.salt
                it[NormalUserTable.fullName] = fullName
                it[NormalUserTable.email] = email
            }
            true
        }
    }

    override suspend fun isUserNameExists(userName: String): Boolean {
        return dbQuery {
            NormalUserTable.select {
                NormalUserTable.userName eq userName
            }.count() > 0
        }
    }

    override suspend fun isUserExist(userId: Long): Boolean {
        return dbQuery {
            val user = NormalUserTable.select { NormalUserTable.id eq userId }.singleOrNull()
            user != null
        }
    }

    override suspend fun getUserByName(userName: String): User {
        return dbQuery {
            NormalUserTable.select(NormalUserTable.userName eq userName).map {
                User(
                    userId = it[NormalUserTable.id].value,
                    userName = it[NormalUserTable.userName],
                    password = it[NormalUserTable.password],
                    salt = it[NormalUserTable.salt]
                )
            }.single()
        }
    }

    override suspend fun isEmailExists(email: String): Boolean {
        return dbQuery {
            NormalUserTable.select { NormalUserTable.email eq email }.singleOrNull() != null
        }
    }
    //endregion

    //region cart

    override suspend fun createCart(userId: Long): Long {
        return dbQuery {
            val newCart = CartTable.insert {
                it[CartTable.userId] = userId
            }
            newCart[CartTable.id].value
        }
    }


    override suspend fun getCartId(userId: Long): Long? {
        return dbQuery {
            CartTable.select { CartTable.userId eq userId }.map { it[CartTable.id].value }.singleOrNull()
        }
    }

    override suspend fun addToCart(cartId: Long, marketId: Long, productId: Long, count: Int): Boolean {
        return dbQuery {
            CartProductTable.insert {
                it[CartProductTable.cartId] = cartId
                it[CartProductTable.productId] = productId
                it[CartProductTable.marketId] = marketId
                it[CartProductTable.count] = count
            }
            true
        }
    }

    override suspend fun isProductInCart(cartId: Long, productId: Long): Boolean {
        return dbQuery {
            val product =
                CartProductTable.select { (CartProductTable.cartId eq cartId) and (CartProductTable.productId eq productId) }
                    .singleOrNull()
            product != null
        }
    }

    override suspend fun getCart(cartId: Long): Cart {
        return dbQuery {
            var total = 0.0
            val products = CartProductTable.select { CartProductTable.cartId eq cartId }
                .map { productRow ->
                    val product = getProduct(productId = productRow[CartProductTable.productId].value)
                    val cartProduct = ProductInCart(
                        id = product.id,
                        name = product.name,
                        price = product.price,
                        count = productRow[CartProductTable.count]
                    )
                    total += product.price * cartProduct.count

                    cartProduct
                }
            Cart(products = products, total = total)
        }
    }

    private suspend fun getProduct(productId: Long): Product {
        return dbQuery {
            ProductTable.select { ProductTable.id eq productId }.map { productRow ->
                productRow.toProduct()
            }.single()
        }
    }

    override suspend fun deleteProductInCart(cartId: Long, productId: Long): Boolean {
        return dbQuery {
            CartProductTable.deleteWhere { (CartProductTable.cartId eq cartId) and (CartProductTable.productId eq productId) }
            true
        }
    }

    override suspend fun deleteAllProductsInCart(cartId: Long):Boolean {
        return dbQuery {
            CartProductTable.deleteWhere { CartProductTable.cartId eq cartId }
            true
        }
    }

    override suspend fun updateCount(cartId: Long, productId: Long, count: Int): Boolean {
        return dbQuery {
            CartProductTable.update({ (CartProductTable.cartId eq cartId) and (CartProductTable.productId eq productId) }) {
                it[CartProductTable.count] = count
            }
            true
        }
    }

    //endregion

    //region wishList
    override suspend fun getWishListId(userId: Long): Long? = dbQuery {
        WishListTable.select { WishListTable.userId eq userId }.map { it[WishListTable.id].value }.singleOrNull()
    }

    override suspend fun addProductToWishList(wishListId: Long, productId: Long): Boolean {
        return dbQuery {
            WishListProductTable.insert {
                it[WishListProductTable.wishListId] = wishListId
                it[WishListProductTable.productId] = productId
            }
            true
        }
    }

    override suspend fun isProductInWishList(wishListId: Long, productId: Long): Boolean {
        return dbQuery {
            val product =
                WishListProductTable.select { (WishListProductTable.wishListId eq wishListId) and (WishListProductTable.productId eq productId) }
                    .singleOrNull()
            product != null
        }
    }

    override suspend fun createWishList(userId: Long): Long {
        return dbQuery {
            val newWishList = WishListTable.insert {
                it[WishListTable.userId] = userId
            }
            newWishList[WishListTable.id].value
        }
    }

    override suspend fun deleteProductFromWishList(wishListId: Long, productId: Long): Boolean {
        return dbQuery {
            WishListProductTable.deleteWhere { (WishListProductTable.wishListId eq wishListId) and
                    (WishListProductTable.productId eq productId) }
            true
        }
    }

    override suspend fun getWishList(wishListId: Long): List<ProductInWishList> = dbQuery {
        WishListProductTable.select { (WishListProductTable.wishListId eq wishListId) and
                (WishListProductTable.isDeleted eq false)}
            .map { productRow ->
                val product = getProduct(productId = productRow[WishListProductTable.productId].value)
                ProductInWishList(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                )
            }
    }

    //endregion

}