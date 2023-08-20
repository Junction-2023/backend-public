package team.f5.api.controller.response

import team.f5.api.entity.ProductEntity

data class ListProductsResponse(
    val products: List<ListProductDetailView>,
    val totalPage: Long,
    val totalCount: Long
)

data class ListProductDetailView(
    val id: Long,
    val productCode: String,
    val productImageUrl: String,
    val name: String,
    val category: String,
    val subCategory: String,
    val price: Long,
    val accumulatedReviews: Long,
    val averageStarRating: Double
) {
    companion object {
        fun from(productEntity: ProductEntity): ListProductDetailView {
            return ListProductDetailView(
                id = productEntity.id,
                productImageUrl = productEntity.productImageUrl,
                productCode = productEntity.productCode,
                name = productEntity.name,
                category = productEntity.category,
                subCategory = productEntity.subCategory,
                price = productEntity.price,
                accumulatedReviews = productEntity.reviews.count().toLong(),
                averageStarRating = productEntity.reviews.map { it.rating }.average().takeIf { !it.isNaN() } ?: 0.0
            )
        }
    }
}
