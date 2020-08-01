package com.ovle.rll3.persistance

import com.badlogic.ashley.core.Component
import java.io.Serializable

data class StoredEntity(
    val id: String,
    val components: Collection<Component>
): Serializable