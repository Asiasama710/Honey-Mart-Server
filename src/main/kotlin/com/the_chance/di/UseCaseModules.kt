package com.the_chance.di

import com.thechance.core.domain.usecase.RefreshTokenUseCase
import com.thechance.core.domain.usecase.admin.AdminUseCaseContainer
import com.thechance.core.domain.usecase.admin.ApproveMarketUseCase
import com.thechance.core.domain.usecase.admin.GetUnApprovedMarkets
import com.thechance.core.domain.usecase.admin.VerifyAdminUseCase
import com.thechance.core.domain.usecase.cart.*
import com.thechance.core.domain.usecase.category.*
import com.thechance.core.domain.usecase.coupon.*
import com.thechance.core.domain.usecase.deviceToken.SaveDeviceTokenUseCase
import com.thechance.core.domain.usecase.market.*
import com.thechance.core.domain.usecase.notification.GetNotificationHistoryUseCase
import com.thechance.core.domain.usecase.notification.SendNotificationOnOrderStateUseCase
import com.thechance.core.domain.usecase.order.*
import com.thechance.core.domain.usecase.owner.CreateOwnerUseCase
import com.thechance.core.domain.usecase.owner.GetOwnerProfileUseCase
import com.thechance.core.domain.usecase.owner.LoginMarketOwnerUseCase
import com.thechance.core.domain.usecase.owner.OwnerUseCaseContainer
import com.thechance.core.domain.usecase.product.*
import com.thechance.core.domain.usecase.user.*
import com.thechance.core.domain.usecase.wishlist.AddProductToWishListUseCase
import com.thechance.core.domain.usecase.wishlist.DeleteProductFromWishListUseCase
import com.thechance.core.domain.usecase.wishlist.GetWishListUseCase
import com.thechance.core.domain.usecase.wishlist.WishListUseCaseContainer
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val productUseCaseModule = module {
    singleOf(::ProductUseCasesContainer) { bind<ProductUseCasesContainer>() }
    singleOf(::CreateProductUseCase) { bind<CreateProductUseCase>() }
    singleOf(::DeleteProductUseCase) { bind<DeleteProductUseCase>() }
    singleOf(::UpdateProductUseCase) { bind<UpdateProductUseCase>() }
    singleOf(::UpdateProductCategoryUseCase) { bind<UpdateProductCategoryUseCase>() }
    singleOf(::GetCategoriesForProductUseCase) { bind<GetCategoriesForProductUseCase>() }
    singleOf(::AddImageProductUseCase) { bind<AddImageProductUseCase>() }
    singleOf(::DeleteImageFromProductUseCase) { bind<DeleteImageFromProductUseCase>() }
    singleOf(::GetProductDetailsUseCase) { bind<GetProductDetailsUseCase>() }
    singleOf(::SearchProductsByNameUseCase) { bind<SearchProductsByNameUseCase>() }
    singleOf(::GetMostRecentProductsUseCase) { bind<GetMostRecentProductsUseCase>() }
    singleOf(::GetAllProductsUseCase) { bind<GetAllProductsUseCase>() }
    singleOf(::UpdateProductImageUseCase) { bind<UpdateProductImageUseCase>() }
}

val marketUseCaseModule = module {
    singleOf(::MarketUseCaseContainer) { bind<MarketUseCaseContainer>() }
    singleOf(::CreateMarketUseCase) { bind<CreateMarketUseCase>() }
    singleOf(::DeleteMarketUseCase) { bind<DeleteMarketUseCase>() }
    singleOf(::GetMarketsUseCase) { bind<GetMarketsUseCase>() }
    singleOf(::UpdateMarketUseCase) { bind<UpdateMarketUseCase>() }
    singleOf(::GetCategoriesByMarketIdUseCase) { bind<GetCategoriesByMarketIdUseCase>() }
    singleOf(::GetMarketDetailsUseCase) { bind<GetMarketDetailsUseCase>() }
    singleOf(::UpdateMarketStatusUseCase) { bind<UpdateMarketStatusUseCase>() }
}

val categoryUseCaseModule = module {
    singleOf(::CategoryUseCasesContainer) { bind<CategoryUseCasesContainer>() }
    singleOf(::CreateCategoryUseCase) { bind<CreateCategoryUseCase>() }
    singleOf(::DeleteCategoryUseCase) { bind<DeleteCategoryUseCase>() }
    singleOf(::GetAllProductsInCategoryUseCase) { bind<GetAllProductsInCategoryUseCase>() }
    singleOf(::UpdateCategoryUseCase) { bind<UpdateCategoryUseCase>() }
}
val wishListUseCaseModule = module {
    singleOf(::WishListUseCaseContainer) { bind<WishListUseCaseContainer>() }
    singleOf(::AddProductToWishListUseCase) { bind<AddProductToWishListUseCase>() }
    singleOf(::GetWishListUseCase) { bind<GetWishListUseCase>() }
    singleOf(::DeleteProductFromWishListUseCase) { bind<DeleteProductFromWishListUseCase>() }
}

val orderUseCaseModule = module {
    singleOf(::OrderUseCasesContainer) { bind<OrderUseCasesContainer>() }
    singleOf(::CreateOrderUseCase) { bind<CreateOrderUseCase>() }
    singleOf(::GetOrdersForMarketUseCase) { bind<GetOrdersForMarketUseCase>() }
    singleOf(::GetOrdersForUserUseCase) { bind<GetOrdersForUserUseCase>() }
    singleOf(::UpdateOrderStateUseCase) { bind<UpdateOrderStateUseCase>() }
    singleOf(::GetOrderDetailsUseCase) { bind<GetOrderDetailsUseCase>() }
}

val userUseCaseModule = module {
    singleOf(::UserUseCaseContainer) { bind<UserUseCaseContainer>() }
    singleOf(::CreateUserUseCase) { bind<CreateUserUseCase>() }
    singleOf(::VerifyUserUseCase) { bind<VerifyUserUseCase>() }
    singleOf(::SaveUserProfileImageUseCase) { bind<SaveUserProfileImageUseCase>() }
    singleOf(::GetUserProfileUseCase) { bind<GetUserProfileUseCase>() }

}

val ownerUseCaseModule = module {
    singleOf(::OwnerUseCaseContainer) { bind<OwnerUseCaseContainer>() }
    singleOf(::CreateOwnerUseCase) { bind<CreateOwnerUseCase>() }
    singleOf(::LoginMarketOwnerUseCase) { bind<LoginMarketOwnerUseCase>() }
    singleOf(::GetOwnerProfileUseCase) { bind<GetOwnerProfileUseCase>() }
}

val adminUseCaseModule = module {
    singleOf(::VerifyAdminUseCase) { bind<VerifyAdminUseCase>() }
    singleOf(::GetUnApprovedMarkets) { bind<GetUnApprovedMarkets>() }
    singleOf(::ApproveMarketUseCase) { bind<ApproveMarketUseCase>() }
    singleOf(::AdminUseCaseContainer) { bind<AdminUseCaseContainer>() }
}

val cartUseCase = module {
    singleOf(::GetCartUseCase) { bind<GetCartUseCase>() }
    singleOf(::AddProductToCartUseCase) { bind<AddProductToCartUseCase>() }
    singleOf(::DeleteProductInCartUseCase) { bind<DeleteProductInCartUseCase>() }
    singleOf(::CartUseCasesContainer) { bind<CartUseCasesContainer>() }
    singleOf(::DeleteCartUseCase) { bind<DeleteCartUseCase>() }
}

val tokenUseCase = module {
    singleOf(::RefreshTokenUseCase) { bind<RefreshTokenUseCase>() }
}

val notificationUseCase = module {
    singleOf(::SendNotificationOnOrderStateUseCase) { bind<SendNotificationOnOrderStateUseCase>() }
    singleOf(::GetNotificationHistoryUseCase) { bind<GetNotificationHistoryUseCase>() }
}

val deviceTokenUseCase = module {
    singleOf(::SaveDeviceTokenUseCase) { bind<SaveDeviceTokenUseCase>() }
    singleOf(::SaveDeviceTokenUseCase) { bind<SaveDeviceTokenUseCase>() }
}

val couponsUseCase = module {
    singleOf(::CouponUseCaseContainer) { bind<CouponUseCaseContainer>() }
    singleOf(::CreateCouponUseCase) { bind<CreateCouponUseCase>() }
    singleOf(::GetValidCouponsUseCase) { bind<GetValidCouponsUseCase>() }
    singleOf(::GetAllCouponsForUserUseCase) { bind<GetAllCouponsForUserUseCase>() }
    singleOf(::GetAllClippedCouponsForUserUseCase) { bind<GetAllClippedCouponsForUserUseCase>() }
    singleOf(::GetAllCouponsForMarketUseCase) { bind<GetAllCouponsForMarketUseCase>() }
    singleOf(::ClipCouponUseCase) { bind<ClipCouponUseCase>() }
}