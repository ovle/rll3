package com.ovle.rll3.model.module.core.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.Mappers.id
import com.ovle.rll3.model.module.core.component.Mappers.perception
import com.ovle.rll3.view.noVisibilityFilter
import ktx.ashley.get
import ktx.ashley.has

fun Component.print(): String {
    return this.toString()
}