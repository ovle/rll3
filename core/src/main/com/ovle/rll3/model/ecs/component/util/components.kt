package com.ovle.rll3.model.ecs.component.util

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.ecs.component.advanced.*
import com.ovle.rll3.model.ecs.component.basic.*
import com.ovle.rll3.model.ecs.component.dto.AOEData
import com.ovle.rll3.model.template.entity.EntityTemplate

typealias ComponentData = Map<String, Any?>
typealias ComponentFactory = (ComponentData?) -> Component

private val componentsMapper: Map<String, ComponentFactory> = mapOf(
    "light" to { value -> LightSourceComponent(AOEData(value!!["radius"] as Int)) },
    "collision" to { value ->  CollisionComponent(
        passable4Body = value?.get("passable4Body") as Boolean? ?: false,
        passable4Light = value?.get("passable4Light") as Boolean? ?: true
    ) },
    "move" to { _ ->  MoveComponent() },
    "living" to { value -> LivingComponent(
        maxHealth = value!!["health"] as Int,
        maxStamina = value!!["stamina"] as Int,
        race = value["race"]?.run { Race.values().find { it.value == this } }
    ).apply {
        health = maxHealth
        stamina = maxStamina
    }},
    "perception" to { value -> PerceptionComponent(value!!["sight"] as Int? ?: 5) },
    "door" to { _ -> DoorComponent() },
    "stash" to { _ -> StashComponent() },
    "container" to { _ -> ContainerComponent() },
    "ai" to { value -> AIComponent(AIType.valueOf((value!!["type"] as String).capitalize())) }
)


fun basicComponents(template: EntityTemplate): List<Component> {
    //todo items doesn't have anything but template
    return listOfNotNull(
        PositionComponent(),
        TemplateComponent(template),
        template.sprite?.run { RenderComponent() },
        ActionComponent()
    )
}

fun stateComponents(template: EntityTemplate) =
    template.state.map {
        (k, v) -> (componentsMapper[k] ?: error("no mapper found for key $k")).invoke(v as ComponentData?)
    }