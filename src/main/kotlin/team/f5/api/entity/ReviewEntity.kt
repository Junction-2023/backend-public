package team.f5.api.entity

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "review")
class ReviewEntity(
    id: Long = 0L,

    @Column(name = "profile_image_url")
    var profileImageUrl: String,

    @Column(name = "user_name")
    val userName: String,

    @Column(name = "rating")
    val rating: Long,

    @Column(name = "content")
    val content: String,

    @Column(name = "is_active")
    var isActive: Boolean,

    @Column(name = "review_date")
    val reviewDate: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: ProductEntity,

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    val reviewImages: MutableList<ReviewImageEntity> = mutableListOf()
) : BaseEntity(id)
