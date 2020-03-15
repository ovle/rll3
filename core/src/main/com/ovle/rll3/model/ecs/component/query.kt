package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import ktx.ashley.has


private inline fun <reified T : Component> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

inline fun <reified T : Component> Entity.has() = this.has(ComponentMapper.getFor(T::class.java))

object Mappers {
    val world = componentMapper<WorldComponent>()
    val level = componentMapper<LevelComponent>()
    val playerInteraction = componentMapper<PlayerInteractionComponent>()
    val levelConnection = componentMapper<LevelConnectionComponent>()
    val position = componentMapper<PositionComponent>()
    val collision = componentMapper<CollisionComponent>()
    val light = componentMapper<LightComponent>()
    val door = componentMapper<DoorComponent>()
    val trigger = componentMapper<TriggerComponent>()
    val sight = componentMapper<SightComponent>()
    val move = componentMapper<MoveComponent>()
    val render = componentMapper<RenderComponent>()
    val animation = componentMapper<AnimationComponent>()
}