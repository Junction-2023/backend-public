package team.f5.api.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "product")
class ProductEntity(
    id: Long = 0L,

    @Column(name = "product_code")
    var productCode: String,

    @Column(name = "category")
    var category: String,

    @Column(name = "sub_category")
    var subCategory: String,

    @Column(name = "price")
    var price: Long,

    @Column(name = "qr_link_url")
    var qrLinkUrl: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "display_review_count")
    var displayReviewCount: Long,

    @Column(name = "product_image_url")
    var productImageUrl: String,

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val options: MutableList<OptionEntity> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val reviews: MutableList<ReviewEntity> = mutableListOf(),

    @Column(name = "display_time")
    var displayTime: Long
) : BaseEntity(id)
