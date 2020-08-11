package com.ovle.rll3.model.module.core.component

import com.badlogic.ashley.core.Component
import com.ovle.rll3.ComponentData
import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.ResourceAmount
import com.ovle.rll3.model.module._deprecated.DoorComponent
import com.ovle.rll3.model.module._deprecated.StashComponent
import com.ovle.rll3.model.module.light.AOEData
import com.ovle.rll3.model.module.ai.AIType
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.collision.CollisionComponent
import com.ovle.rll3.model.module.container.ContainerComponent
import com.ovle.rll3.model.module.entityAction.EntityActionComponent
import com.ovle.rll3.model.module.gathering.ResourceComponent
import com.ovle.rll3.model.module.gathering.ResourceType
import com.ovle.rll3.model.module.gathering.SourceComponent
import com.ovle.rll3.model.module.health.HealthComponent
import com.ovle.rll3.model.module.light.LightSourceComponent
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.module.render.RenderComponent
import com.ovle.rll3.model.module.space.MoveComponent
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.entityViewTemplate


private val componentsMapper: Map<String, ComponentFactory> = mapOf(
    "light" to { value -> LightSourceComponent(AOEData(value!!["radius"] as Int)) },
    "collision" to { value ->
        CollisionComponent(
            passable4Body = value?.get("passable4Body") as Boolean? ?: false,
            passable4Light = value?.get("passable4Light") as Boolean? ?: true
        )
    },
    "move" to { _ -> MoveComponent() },
    "living" to { value ->
        HealthComponent(
            maxHealth = value!!["health"] as Int,
            maxStamina = value["stamina"] as Int
        ).apply {
            health = maxHealth
            stamina = maxStamina
        }
    },
    "perception" to { value ->
        PerceptionComponent(value!!["sight"] as Int? ?: 5)
    },
    "door" to { _ -> DoorComponent() },
    "stash" to { _ -> StashComponent() },
    "container" to { _ -> ContainerComponent() },
    "ai" to { value -> AIComponent(AIType.valueOf((value!!["type"] as String).capitalize())) },
    "task" to { _ -> TaskPerformerComponent() },
    "resource" to { _ -> ResourceComponent() },
    "source" to { value ->
        SourceComponent(
            type = ResourceType.valueOf((value!!["type"] as String).capitalize()),
            amount = value["amount"] as ResourceAmount
        )
    }
)


fun basicComponents(template: EntityTemplate): List<Component> {
    val viewTemplate = entityViewTemplate(name = template.name)
    //todo items doesn't have anything but template
    return listOfNotNull(
        PositionComponent(),
        TemplateComponent(template, viewTemplate),
        viewTemplate?.sprite?.run { RenderComponent() },
        EntityActionComponent()
    )
}

fun stateComponents(template: EntityTemplate) =
    template.state.map { (k, v) ->
        (componentsMapper[k] ?: error("no mapper found for key $k")).invoke(v as ComponentData?)
    }