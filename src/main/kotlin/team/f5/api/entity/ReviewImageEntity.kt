package team.f5.api.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "review_image")
class ReviewImageEntity(
    id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    val review: ReviewEntity,

    @Column(name = "image_url")
    var imageUrl: String
) : BaseEntity()
