package team.f5.api.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.f5.api.dto.ReviewSummary
import team.f5.api.entity.ProductEntity
import team.f5.api.entity.ReviewEntity

@Repository
interface ReviewRepository : JpaRepository<ReviewEntity, Long> {
    @Query(
        """
        SELECT new team.f5.api.dto.ReviewSummary(
            r.rating,
            COUNT(r)
        )
        FROM ReviewEntity r
        WHERE r.product.id = :productId
        GROUP BY r.rating
        """
    )
    fun findReviewSummaryByProductId(productId: Long): List<ReviewSummary>
    fun findByIdInAndProduct(reviewIds: List<Long>, product: ProductEntity): List<ReviewEntity>

    @Query(
        """
        SELECT r
        FROM ReviewEntity r
        LEFT JOIN FETCH r.reviewImages
        WHERE r.product = :product
        AND (
            (:type = 'USERNAME' AND r.userName = :keyword) OR 
            (:type = 'CONTENT' AND r.content LIKE CONCAT('%', :keyword, '%')) OR 
            :type IS NULL OR :keyword IS NULL OR :keyword = ''
        )
        AND r.isActive = :isVisible
        AND r.id < :cursor
        ORDER BY r.id DESC
        """
    )
    fun search(
        product: ProductEntity,
        isVisible: Boolean,
        keyword: String?,
        type: String?,
        cursor: Long,
        pageable: Pageable
    ): List<ReviewEntity>

    @Query(
        """
        SELECT COUNT(r.id)
        FROM ReviewEntity r
        WHERE r.product = :product
        AND (
            (:type = 'USERNAME' AND r.userName = :keyword) OR 
            (:type = 'CONTENT' AND r.content LIKE CONCAT('%', :keyword, '%')) OR 
            :type IS NULL OR :keyword IS NULL OR :keyword = ''
        )
        AND r.isActive = :isVisible
        """
    )
    fun countTotalForSearch(
        product: ProductEntity,
        isVisible: Boolean,
        keyword: String?,
        type: String?
    ): Long
}
