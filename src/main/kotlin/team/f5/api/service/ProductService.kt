package team.f5.api.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.f5.api.controller.request.ProductUpdateRequestDto
import team.f5.api.controller.request.ReviewKeywordType
import team.f5.api.controller.response.ListProductDetailView
import team.f5.api.controller.response.ListProductsResponse
import team.f5.api.controller.response.ReviewSearchResponseDto
import team.f5.api.controller.response.ReviewSummaryResponseDto
import team.f5.api.entity.ProductEntity
import team.f5.api.repository.ProductRepository
import team.f5.api.repository.ReviewRepository
import team.f5.api.support.exception.ApplicationException
import team.f5.api.support.exception.ErrorType

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository
) {
    fun getProductById(productId: Long): ProductEntity {
        return productRepository.findByIdOrNull(productId)
            ?: throw ApplicationException(ErrorType.NOT_FOUND)
    }

    fun getReviewsSummaryById(productId: Long): ReviewSummaryResponseDto {
        val product = getProductById(productId)
        val reviewSummaries = reviewRepository.findReviewSummaryByProductId(productId)
        return ReviewSummaryResponseDto(
            reviewSummaries = reviewSummaries,
            productName = product.name
        )
    }

    fun listProducts(
        searchKeyword: String?,
        category: String?,
        subCategory: String?,
        page: Int,
        size: Int
    ): ListProductsResponse {
        // client로부터 page 1로 받음
        val offset = (page - 1) * size

        val searchProducts = productRepository.searchProducts(
            searchKeyword = searchKeyword,
            category = category,
            subCategory = subCategory,
            offset = offset,
            size = size
        )

        val productTotalCount = productRepository.searchProductCount(searchKeyword, subCategory, category)

        val listProductDetailViews = searchProducts.map {
            ListProductDetailView.from(it)
        }

        return ListProductsResponse(
            products = listProductDetailViews,
            totalPage = if (productTotalCount % size == 0L) productTotalCount / size else productTotalCount / size + 1,
            totalCount = productTotalCount
        )
    }

    fun getReviewList(
        productId: Long,
        isVisible: Boolean,
        keyword: String?,
        keywordType: ReviewKeywordType?,
        cursor: Long?,
        page: Int?,
        size: Int
    ): ReviewSearchResponseDto {
        val product = getProductById(productId)
        val reviews = reviewRepository.search(
            product = product,
            isVisible = isVisible,
            keyword = keyword,
            type = keywordType?.name,
            cursor = if (page != null || cursor == null) Long.MAX_VALUE else cursor,
            pageable = PageRequest.of(page ?: 0, size + 1)
        )
        val hasNext = reviews.size > size && reviews.lastOrNull() != null

        return ReviewSearchResponseDto.from(
            if (hasNext) reviews.subList(0, reviews.size - 1) else reviews,
            hasNext,
            reviewRepository.countTotalForSearch(
                product = product,
                isVisible = isVisible,
                keyword = keyword,
                type = keywordType?.name
            )
        )
    }

    @Transactional
    fun update(productId: Long, productUpdateRequestDto: ProductUpdateRequestDto) {
        val product = productRepository.findByIdOrNull(productId) ?: throw ApplicationException(ErrorType.NOT_FOUND)
        product.options.forEach {
            it.isActive = productUpdateRequestDto.selectedOptionIds.contains(it.id)
        }
        product.displayTime = productUpdateRequestDto.displayTime
        product.displayReviewCount = productUpdateRequestDto.displayReviewCount
    }
}
