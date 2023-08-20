package team.f5.api.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "product_option")
class OptionEntity(

    id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: ProductEntity,

    @Column(name = "option_name")
    val optionName: String,

    @Column(name = "option_value")
    var optionValue: Double,

    @Column(name = "is_active")
    var isActive: Boolean
) : BaseEntity(id)
