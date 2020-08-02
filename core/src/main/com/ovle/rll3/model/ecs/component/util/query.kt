package com.ovle.rll3.model.ecs.component.util

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.component.advanced.*
import com.ovle.rll3.model.ecs.component.basic.*
import com.ovle.rll3.model.ecs.component.special.*
import ktx.ashley.has


private inline fun <reified T : BaseComponent> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

inline fun <reified T : BaseComponent> Entity.has() = this.has(ComponentMapper.getFor(T::class.java))

object Mappers {
    val id = componentMapper<IdComponent>()
    val world = componentMapper<WorldComponent>()
    val level = componentMapper<LevelComponent>()
    val playerInteraction = componentMapper<PlayerInteractionComponent>()
    val levelConnection = componentMapper<LevelConnectionComponent>()
    val position = componentMapper<PositionComponent>()
    val template = componentMapper<TemplateComponent>()
    val collision = componentMapper<CollisionComponent>()
    val light = componentMapper<LightSourceComponent>()
    val perception = componentMapper<PerceptionComponent>()
    val move = componentMapper<MoveComponent>()
    val render = componentMapper<RenderComponent>()
    val living = componentMapper<LivingComponent>()
    val action = componentMapper<ActionComponent>()
    val container = componentMapper<ContainerComponent>()
    val ai = componentMapper<AIComponent>()
    val questOwner = componentMapper<QuestOwnerComponent>()
    val taskPerformer = componentMapper<TaskPerformerComponent>()

    //todo remove these
    val door = componentMapper<DoorComponent>()
    val stash = componentMapper<StashComponent>()
    val player = componentMapper<PlayerComponent>()
}