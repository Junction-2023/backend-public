package team.f5.api.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.f5.api.controller.request.ProductUpdateRequestDto
import team.f5.api.controller.request.ReviewKeywordType
import team.f5.api.controller.request.ReviewStatusUpdateRequestDto
import team.f5.api.controller.response.ListProductsResponse
import team.f5.api.controller.response.ProductDetailResponseDto
import team.f5.api.controller.response.ReviewSearchResponseDto
import team.f5.api.controller.response.ReviewSummaryResponseDto
import team.f5.api.service.ProductService
import team.f5.api.service.ReviewService
import team.f5.api.support.exception.ApplicationException
import team.f5.api.support.exception.ErrorType
import team.f5.api.support.response.ApiResponse
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
    private val reviewService: ReviewService
) {
    @GetMapping("/{productId}")
    fun getProductById(
        @PathVariable productId: Long
    ): ApiResponse<ProductDetailResponseDto> {
        val product = productService.getProductById(productId)
        return ApiResponse.success(ProductDetailResponseDto.from(product))
    }

    @GetMapping
    fun listProducts(
        @RequestParam(name = "searchKeyword") searchKeyword: String?,
        @RequestParam(name = "category") category: String?,
        @RequestParam(name = "subCategory") subCategory: String?,
        @Min(1)
        @RequestParam(name = "page", defaultValue = "1")
        page: Int,
        @Min(1)
        @RequestParam(name = "size", defaultValue = "10")
        size: Int
    ): ApiResponse<ListProductsResponse> {
        val listProductsResponse = productService.listProducts(searchKeyword, category, subCategory, page, size)
        return ApiResponse.success(listProductsResponse)
    }

    @GetMapping("/{productId}/reviews-summary")
    fun getReviewsSummaryById(
        @PathVariable productId: Long
    ): ApiResponse<ReviewSummaryResponseDto> {
        val result = productService.getReviewsSummaryById(productId)
        return ApiResponse.success(result)
    }

    @GetMapping("/{productId}/reviews")
    fun getReviewsByProductId(
        @PathVariable productId: Long,
        @RequestParam(name = "type") keywordType: ReviewKeywordType?,
        @RequestParam(name = "keyword") keyword: String?,
        @RequestParam("isVisible") isVisible: Boolean,
        @RequestParam("cursor") cursor: Long?,
        @RequestParam("page") page: Int?,
        @RequestParam("size") size: Int
    ): ApiResponse<ReviewSearchResponseDto> {
        if (page != null && cursor != null) {
            throw ApplicationException(ErrorType.ILLEGAL_ARGUMENT, "page와 cursor는 동시에 사용할 수 없습니다.")
        }

        val result = productService.getReviewList(
            productId,
            isVisible,
            keyword,
            keywordType,
            cursor,
            page,
            size
        )
        return ApiResponse.success(result)
    }

    @PatchMapping("/{productId}/reviews")
    fun updateReviewStatus(
        @PathVariable("productId")
        productId: Long,
        @RequestBody
        reviewStatusUpdateRequestDto: ReviewStatusUpdateRequestDto
    ): ApiResponse<Unit> {
        reviewService.updateReviewStatus(productId, reviewStatusUpdateRequestDto)
        return ApiResponse.success()
    }

    @PatchMapping("/{productId}")
    fun updateOption(
        @PathVariable("productId") productId: Long,
        @RequestBody productUpdateRequestDto: ProductUpdateRequestDto
    ): ApiResponse<Unit> {
        productService.update(productId, productUpdateRequestDto)
        return ApiResponse.success()
    }
}
