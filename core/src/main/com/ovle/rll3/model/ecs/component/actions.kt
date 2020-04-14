package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.util.has

enum class EntityInteraction(
    val check: (entity: Entity) -> Boolean
) {
    Investigate({ true }),
    Attack({ it.has<LivingComponent>() }),
    Talk({ it.has<LivingComponent>() }),
    Use({ it.has<DoorComponent>() }),
    Travel({ it.has<LevelConnectionComponent>() });
}

fun actions(entity: Entity): Collection<EntityInteraction> = EntityInteraction.values()
    .filter {
        it.check(entity)
    }
