package com.ovle.rll3.model.ecs.system.interaction

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.advanced.ContainerComponent
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent
import com.ovle.rll3.model.ecs.component.util.has

enum class EntityInteraction(
    val actionName: String,
    val check: (entity: Entity) -> Boolean
) {
    Investigate("investigate", { true }),
    Combat("combat", { it.has<LivingComponent>() }),
    Talk("talk", { it.has<LivingComponent>() }),
    Use("use", {
        it.has<DoorComponent>() || it.has<ContainerComponent>()}
    ),   //todo
    Travel("travel", { it.has<LevelConnectionComponent>() });
}