package team.f5.api.service

import org.springframework.stereotype.Service
import team.f5.api.controller.response.CategoryResponseDto
import team.f5.api.repository.ProductRepository

@Service
class CommonService(
    private val productRepository: ProductRepository
) {
    fun getCategories(): List<CategoryResponseDto> {
        return productRepository.findAll().groupBy { it.category }.map {
            CategoryResponseDto(
                it.key,
                it.value.map { v -> v.subCategory }.distinct()
            )
        }
    }
}
