package team.f5.api.controller.response

import team.f5.api.dto.ReviewSummary

data class ReviewSummaryResponseDto(
    val reviewSummaries: List<ReviewSummary>,
    val productName: String
)
