package team.f5.api.controller.response

data class CategoryResponseDto(
    val category: String,
    val subCategories: List<String>
)
