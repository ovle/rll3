package com.ovle.rll3.persistence

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module.core.entity.id
import java.io.Serializable

data class StoredEntity(
    val id: String,
    val components: Collection<Component>
): Serializable

fun Entity.stored() = StoredEntity(this.id(), this.components.toList())