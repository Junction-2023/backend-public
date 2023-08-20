package team.f5.api.controller.request

data class ProductUpdateRequestDto(
    val selectedOptionIds: List<Long>,
    val displayReviewCount: Long,
    val displayTime: Long
)
