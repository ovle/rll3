package com.ovle.rll3.model.module.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.entity.isExists
import com.ovle.rll3.model.module.core.entity.position


class TaskTarget(val target: Any?) {
    fun asEntity() = (target as Entity).also { validate() }
    fun asPosition() = (target as GridPoint2).also { validate() }

    fun position(): GridPoint2 {
        validate()
        return when (target) {
            is Entity -> target.position()
            is GridPoint2 -> target
            else -> throw IllegalStateException("position not supported by target $target")
        }
    }

    fun isValid() = when (target) {
        is Entity -> target.isExists()
        is GridPoint2 -> true
        else -> throw IllegalStateException("isValid not supported by target $target")
    }

    //todo if skill success is destroying entity then !isExists(t.asEntity()) check will throw an exception
    private fun validate() {
//        if (!isValid()) throw InvalidTargetException()
    }
}

