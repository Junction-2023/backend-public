package team.f5.api.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.f5.api.controller.request.ReviewStatusUpdateRequestDto
import team.f5.api.repository.ProductRepository
import team.f5.api.repository.ReviewRepository
import team.f5.api.support.exception.ApplicationException
import team.f5.api.support.exception.ErrorType

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository
) {

    @Transactional
    fun updateReviewStatus(productId: Long, reviewStatusUpdateRequestDto: ReviewStatusUpdateRequestDto) {
        val product = productRepository.findByIdOrNull(productId)
            ?: throw ApplicationException(ErrorType.NOT_FOUND, "존재하지 않은 상품입니다.")
        val reviewIds = reviewStatusUpdateRequestDto.reviewIds
        val reviews = reviewRepository.findByIdInAndProduct(reviewIds, product)
        if (reviews.size != reviewIds.size) {
            throw ApplicationException(ErrorType.NOT_FOUND, "존재하지 않은 리뷰가 포함되어 있습니다.")
        }

        if (reviews.map { it.isActive }.toSet().size != 1) {
            throw ApplicationException(ErrorType.ILLEGAL_ARGUMENT, "리뷰의 상태가 모두 같아야 합니다.")
        }

        reviews.map { it.isActive = !it.isActive }
    }
}
