package com.ovle.rll3.model.module.core

import com.badlogic.ashley.core.Entity
import com.ovle.rlUtil.gdx.ashley.component.mapper
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.core.component.template.TemplateComponent
import com.ovle.rll3.model.util.info
import ktx.ashley.get
import ktx.ashley.has

object Components {
    val core = mapper<CoreComponent>()
    val template = mapper<TemplateComponent>()
}

fun Entity.id(): EntityId {
    check(this.has(Components.core)) { "no id for entity ${this.info()}" }
    return this[Components.core]!!.id
}

fun Entity.name(): EntityId {
    check(this.has(Components.template)) { "no template for entity ${this.info()}" }
    return this[Components.template]!!.template.name
}