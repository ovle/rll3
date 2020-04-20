package com.ovle.rll3.model.ecs.component.util

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.component.advanced.DoorComponent
import com.ovle.rll3.model.ecs.component.advanced.LightSourceComponent
import com.ovle.rll3.model.ecs.component.advanced.LivingComponent
import com.ovle.rll3.model.ecs.component.advanced.Race
import com.ovle.rll3.model.ecs.component.basic.*
import com.ovle.rll3.model.ecs.component.dto.AOEData
import com.ovle.rll3.model.template.entity.EntityTemplate

typealias ComponentData = Map<String, Any?>
typealias ComponentFactory = (ComponentData?) -> Component

private val componentsMapper: Map<String, ComponentFactory> = mapOf(
    "light" to { value -> LightSourceComponent(AOEData(value!!["radius"] as Int)) },
    "collision" to { _ ->  CollisionComponent() },
    "move" to { _ ->  MoveComponent() },
    "living" to { value -> LivingComponent(
        maxHealth = value!!["health"] as Int,
        maxStamina = value!!["stamina"] as Int,
        race = value["race"]?.run { Race.values().find { it.value == this } }
    ).apply {
        health = maxHealth
        stamina = maxStamina
    }},
    "door" to { _ -> DoorComponent() }
)


fun basicComponents(template: EntityTemplate): List<Component> {
    return listOfNotNull(
        PositionComponent(),
        TemplateComponent(template),
        template.sprite?.run { RenderComponent() },
        if (template.animations.isNotEmpty()) AnimationComponent() else null
    )
}

fun stateComponents(template: EntityTemplate) =
    template.state.map {
        (k, v) -> (componentsMapper[k] ?: error("no mapper found for key $k")).invoke(v as ComponentData?)
    }