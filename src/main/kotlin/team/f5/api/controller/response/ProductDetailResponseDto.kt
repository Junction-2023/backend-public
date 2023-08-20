package team.f5.api.controller.response

import team.f5.api.entity.ProductEntity

data class ProductDetailResponseDto(
    val id: Long,
    val productCode: String,
    val productImageUrl: String,
    val name: String,
    val category: String,
    val subCategory: String,
    val price: Long,
    val qrLinkUrl: String,
    val displayReviewCount: Long,
    val displayTime: Long,
    val displayOptions: List<DisplayOptionResponseDto>
) {
    data class DisplayOptionResponseDto(
        val id: Long,
        val optionName: String,
        val isActive: Boolean
    )

    companion object {
        fun from(productEntity: ProductEntity): ProductDetailResponseDto {
            return with(productEntity) {
                ProductDetailResponseDto(
                    id = id,
                    productCode = productCode,
                    productImageUrl = productImageUrl,
                    name = name,
                    category = category,
                    subCategory = subCategory,
                    price = price,
                    qrLinkUrl = qrLinkUrl,
                    displayReviewCount = displayReviewCount,
                    displayTime = displayTime,
                    displayOptions = options.map { option ->
                        with(option) {
                            DisplayOptionResponseDto(
                                id = id,
                                optionName = optionName,
                                isActive = isActive
                            )
                        }
                    }
                )
            }
        }
    }
}
