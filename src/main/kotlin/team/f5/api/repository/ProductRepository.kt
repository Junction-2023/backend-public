package team.f5.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.f5.api.entity.ProductEntity

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    @Query(
        value = """
            SELECT * FROM product
            WHERE
                (:searchKeyword IS NULL OR name LIKE CONCAT('%', :searchKeyword, '%'))
                AND (:category IS NULL OR category = :category)
                AND (:subCategory IS NULL OR sub_category = :subCategory)
            LIMIT :size OFFSET :offset
        """,
        nativeQuery = true
    )
    fun searchProducts(
        searchKeyword: String?,
        category: String?,
        subCategory: String?,
        offset: Int,
        size: Int
    ): List<ProductEntity>

    @Query(
        value = """
            SELECT COUNT(*) FROM product
            WHERE
                (:searchKeyword IS NULL OR name LIKE CONCAT(:searchKeyword, '%'))
                AND (:category IS NULL OR category = :category)
                AND (:subCategory IS NULL OR sub_category = :subCategory)
        """,
        nativeQuery = true
    )
    fun searchProductCount(
        searchKeyword: String?,
        subCategory: String?,
        category: String?
    ): Long
}
