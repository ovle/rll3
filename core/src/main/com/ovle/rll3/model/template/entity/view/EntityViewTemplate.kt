package com.ovle.rll3.model.template.entity.view

import com.badlogic.gdx.math.GridPoint2

data class EntityViewTemplate(
    var name: String = "",
    var version: String = "0.1",
    var sprite: Map<String, GridPoint2>? = null,
    var portrait: Collection<GridPoint2>? = null
)
