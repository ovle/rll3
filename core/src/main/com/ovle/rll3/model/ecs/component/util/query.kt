package com.ovle.rll3.model.ecs.component.util

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.advanced.*
import com.ovle.rll3.model.ecs.component.basic.*
import com.ovle.rll3.model.ecs.component.special.*
import ktx.ashley.has


private inline fun <reified T : Component> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

inline fun <reified T : Component> Entity.has() = this.has(ComponentMapper.getFor(T::class.java))

object Mappers {
    val world = componentMapper<WorldComponent>()
    val level = componentMapper<LevelComponent>()
    val playerInteraction = componentMapper<PlayerInteractionComponent>()
    val levelConnection = componentMapper<LevelConnectionComponent>()
    val position = componentMapper<PositionComponent>()
    val template = componentMapper<TemplateComponent>()
    val collision = componentMapper<CollisionComponent>()
    val light = componentMapper<LightSourceComponent>()
    val sight = componentMapper<PerceptionComponent>()
    val move = componentMapper<MoveComponent>()
    val render = componentMapper<RenderComponent>()
    val animation = componentMapper<AnimationComponent>()
    val living = componentMapper<LivingComponent>()
    val action = componentMapper<ActionComponent>()

    //todo remove these
    val door = componentMapper<DoorComponent>()
    val player = componentMapper<PlayerComponent>()
}