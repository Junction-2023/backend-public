package team.f5.api.controller.response

import team.f5.api.entity.ReviewEntity
import java.time.LocalDate

data class ReviewSearchResponseDto(
    val reviews: List<ReviewDetail>,
    val hasNext: Boolean,
    val totalCount: Long
) {
    data class ReviewDetail(
        val id: Long,
        val profileImageUrl: String,
        val userName: String,
        val content: String,
        val reviewDate: LocalDate,
        val imageUrls: List<String>,
        val visible: Boolean
    ) {
        companion object {
            fun from(reviewEntity: ReviewEntity): ReviewDetail {
                return ReviewDetail(
                    id = reviewEntity.id,
                    profileImageUrl = reviewEntity.profileImageUrl,
                    userName = reviewEntity.userName,
                    content = reviewEntity.content,
                    reviewDate = reviewEntity.reviewDate,
                    imageUrls = reviewEntity.reviewImages.map { it.imageUrl },
                    visible = reviewEntity.isActive
                )
            }
        }
    }

    companion object {
        fun from(reviewEntities: List<ReviewEntity>, hasNext: Boolean, totalCount: Long): ReviewSearchResponseDto {
            return ReviewSearchResponseDto(
                reviews = reviewEntities.map {
                    ReviewDetail.from(it)
                },
                hasNext = hasNext,
                totalCount = totalCount
            )
        }
    }
}
