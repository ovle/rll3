package com.ovle.rll3.model.module.core.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.ovle.rll3.model.module.core.entity.isExists

abstract class BaseIteratingSystem(family: Family): IteratingSystem(family) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (!entity.isExists()) return
        processEntityIntr(entity, deltaTime)
    }

    abstract fun processEntityIntr(entity: Entity, deltaTime: Float)
}