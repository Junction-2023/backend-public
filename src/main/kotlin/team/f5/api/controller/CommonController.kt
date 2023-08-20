package team.f5.api.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.f5.api.controller.response.CategoryResponseDto
import team.f5.api.service.CommonService
import team.f5.api.support.response.ApiResponse

@Validated
@RestController
@RequestMapping
class CommonController(
    private val commonService: CommonService
) {
    @GetMapping("/categories")
    fun getCategories(): ApiResponse<List<CategoryResponseDto>> {
        return ApiResponse.success(commonService.getCategories())
    }
}
