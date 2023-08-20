package team.f5.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.f5.api.entity.OptionEntity

@Repository
interface OptionRepository : JpaRepository<OptionEntity, Long>
