package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import ktx.ashley.has


inline fun <reified T : Component> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

inline fun <reified T : Component> Entity.has() = this.has(componentMapper<T>())

//inline fun <reified T : Component> Entity.c() = this[componentMapper<T>()]

object Mappers {
    val level: ComponentMapper<LevelComponent> = componentMapper()
    val playerInteraction: ComponentMapper<PlayerInteractionComponent> = componentMapper()
    val levelConnection: ComponentMapper<LevelConnectionComponent> = componentMapper()
    val position: ComponentMapper<PositionComponent> = componentMapper()
    val light: ComponentMapper<LightComponent> = componentMapper()
    val door: ComponentMapper<DoorComponent> = componentMapper()
    val trap: ComponentMapper<TrapComponent> = componentMapper()
    val sight: ComponentMapper<SightComponent> = componentMapper()
    val move: ComponentMapper<MoveComponent> = componentMapper()
    val render: ComponentMapper<RenderComponent> = componentMapper()
    val animation: ComponentMapper<AnimationComponent> = componentMapper()
}