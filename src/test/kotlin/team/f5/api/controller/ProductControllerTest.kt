package team.f5.api.controller

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import team.f5.api.controller.request.ReviewKeywordType
import team.f5.api.entity.OptionEntity
import team.f5.api.entity.ProductEntity
import team.f5.api.entity.ReviewEntity
import team.f5.api.support.response.ApiResponse
import java.time.LocalDate
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceUnit

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class ProductControllerTest @Autowired constructor(
    private val sut: ProductController
) {
    @PersistenceUnit
    private lateinit var emf: EntityManagerFactory

    @Test
    fun `상품 상세 조회에 성공한다`() {
        // given
        val product = save(
            ProductEntity(
                productCode = "abc-123",
                category = "육류",
                subCategory = "소고기",
                price = 10000L,
                qrLinkUrl = "https://www.naver.com",
                name = "한우 양지 100g",
                displayReviewCount = 10L,
                displayTime = 10L,
                productImageUrl = "https://www.naver.com"
            )
        )
        val option1 = save(
            OptionEntity(
                product = product,
                optionName = "누적 판매수",
                optionValue = 100.0,
                isActive = true
            )
        )
        val option2 = save(
            OptionEntity(
                product = product,
                optionName = "실시간 조회수",
                optionValue = 100.0,
                isActive = false
            )
        )

        // when
        val result = sut.getProductById(product.id)

        // then
        result.status shouldBe ApiResponse.ApiStatus.SUCCESS
        result.data shouldNotBe null
        result.data?.id shouldBe product.id
        result.data?.productCode shouldBe product.productCode
        result.data?.name shouldBe product.name
        result.data?.category shouldBe product.category
        result.data?.subCategory shouldBe product.subCategory
        result.data?.price shouldBe product.price
        result.data?.qrLinkUrl shouldBe product.qrLinkUrl
        result.data?.displayReviewCount shouldBe product.displayReviewCount
        result.data?.displayTime shouldBe product.displayTime
        result.data?.displayOptions?.size shouldBe 2
        result.data?.displayOptions?.get(0)?.id shouldBe option1.id
        result.data?.displayOptions?.get(0)?.optionName shouldBe option1.optionName
        result.data?.displayOptions?.get(0)?.isActive shouldBe option1.isActive
        result.data?.displayOptions?.get(1)?.id shouldBe option2.id
        result.data?.displayOptions?.get(1)?.optionName shouldBe option2.optionName
        result.data?.displayOptions?.get(1)?.isActive shouldBe option2.isActive
    }

    @Test
    fun `상품 리뷰 요약 조회에 성공한다`() {
        // given
        val product = save(
            ProductEntity(
                productCode = "abc-124",
                category = "육류",
                subCategory = "소고기",
                price = 10000L,
                qrLinkUrl = "https://www.naver.com",
                name = "한우 살치 100g",
                displayReviewCount = 10L,
                displayTime = 10L,
                productImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 1L,
                content = "최악이에요",
                isActive = true,
                userName = "김현조",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 1L,
                content = "너무너무 이에요",
                isActive = true,
                userName = "김한수",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 2L,
                content = "조금 아쉬워요",
                isActive = true,
                userName = "김성일",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 4L,
                content = "맛있어요",
                isActive = true,
                userName = "고수현",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 5L,
                content = "너무 맛있어요",
                isActive = true,
                userName = "민재원",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )

        // when
        val result = sut.getReviewsSummaryById(product.id)

        // then
        result.status shouldBe ApiResponse.ApiStatus.SUCCESS
        result.data shouldNotBe null
        result.data?.productName shouldBe product.name
        result.data?.reviewSummaries?.size shouldBe 4
        result.data?.reviewSummaries?.get(0)?.rating shouldBe 1L
        result.data?.reviewSummaries?.get(0)?.count shouldBe 2L
        result.data?.reviewSummaries?.get(1)?.rating shouldBe 2L
        result.data?.reviewSummaries?.get(1)?.count shouldBe 1L
        result.data?.reviewSummaries?.get(2)?.rating shouldBe 4L
        result.data?.reviewSummaries?.get(2)?.count shouldBe 1L
        result.data?.reviewSummaries?.get(3)?.rating shouldBe 5L
        result.data?.reviewSummaries?.get(3)?.count shouldBe 1L
    }

    @Test
    fun `상품 리뷰 검색에 성공한다`() {
        // given
        val product = save(
            ProductEntity(
                productCode = "abc-124",
                category = "육류",
                subCategory = "소고기",
                price = 10000L,
                qrLinkUrl = "https://www.naver.com",
                name = "한우 살치 100g",
                displayReviewCount = 10L,
                displayTime = 10L,
                productImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 4L,
                content = "맛있어요",
                isActive = false,
                userName = "고수현",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 4L,
                content = "맛있어요",
                isActive = true,
                userName = "김성일",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 5L,
                content = "너무 맛있어요",
                isActive = true,
                userName = "민재원",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )
        save(
            ReviewEntity(
                product = product,
                rating = 5L,
                content = "너무 너무 맛있어요",
                isActive = true,
                userName = "김한수",
                reviewDate = LocalDate.now(),
                profileImageUrl = "https://www.naver.com"
            )
        )

        // when
        val result1 = sut.getReviewsByProductId(
            productId = product.id,
            cursor = null,
            size = 2,
            isVisible = true,
            keywordType = ReviewKeywordType.CONTENT,
            keyword = "맛있어요"
        )
        val result2 = sut.getReviewsByProductId(
            productId = product.id,
            cursor = null,
            size = 10,
            isVisible = true,
            keywordType = ReviewKeywordType.USERNAME,
            keyword = "민재원"
        )
        val result3 = sut.getReviewsByProductId(
            productId = product.id,
            cursor = null,
            size = 10,
            isVisible = false,
            keywordType = ReviewKeywordType.CONTENT,
            keyword = "맛있어요"
        )
        val result4 = sut.getReviewsByProductId(
            productId = product.id,
            cursor = result1.data?.reviews?.lastOrNull()?.id,
            size = 2,
            isVisible = true,
            keywordType = ReviewKeywordType.CONTENT,
            keyword = "맛있어요"
        )

        // then
        result1.status shouldBe ApiResponse.ApiStatus.SUCCESS
        result1.data shouldNotBe null
        result1.data?.reviews?.size shouldBe 2
        result1.data?.totalCount shouldBe 3
        result1.data?.hasNext shouldBe true
        result2.data?.reviews?.size shouldBe 1
        result2.data?.totalCount shouldBe 1
        result2.data?.hasNext shouldBe false
        result3.data?.reviews?.size shouldBe 1
        result3.data?.totalCount shouldBe 1
        result3.data?.hasNext shouldBe false
        result4.data?.reviews?.size shouldBe 1
        result4.data?.totalCount shouldBe 3
        result4.data?.hasNext shouldBe false
    }

    private fun <T> save(entity: T): T {
        val em = emf.createEntityManager()
        em.transaction.begin()
        em.persist(entity)
        em.transaction.commit()
        em.close()
        return entity
    }
}
