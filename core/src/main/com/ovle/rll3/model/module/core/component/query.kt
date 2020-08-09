package com.ovle.rll3.model.module.core.component

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.module._deprecated.DoorComponent
import com.ovle.rll3.model.module._deprecated.StashComponent
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.collision.CollisionComponent
import com.ovle.rll3.model.module.container.ContainerComponent
import com.ovle.rll3.model.module.entityAction.EntityActionComponent
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.game.PlayerComponent
import com.ovle.rll3.model.module.gathering.ResourceComponent
import com.ovle.rll3.model.module.gathering.SourceComponent
import com.ovle.rll3.model.module.health.LivingComponent
import com.ovle.rll3.model.module.light.LightSourceComponent
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.module.quest.QuestOwnerComponent
import com.ovle.rll3.model.module.render.RenderComponent
import com.ovle.rll3.model.module.space.MoveComponent
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import ktx.ashley.has


private inline fun <reified T : BaseComponent> componentMapper(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)

inline fun <reified T : BaseComponent> Entity.has() = this.has(ComponentMapper.getFor(T::class.java))

object Mappers {
    val id = componentMapper<IdComponent>()
    val level = componentMapper<GameComponent>()
    val playerInteraction = componentMapper<PlayerInteractionComponent>()
    val position = componentMapper<PositionComponent>()
    val template = componentMapper<TemplateComponent>()
    val collision = componentMapper<CollisionComponent>()
    val light = componentMapper<LightSourceComponent>()
    val perception = componentMapper<PerceptionComponent>()
    val move = componentMapper<MoveComponent>()
    val render = componentMapper<RenderComponent>()
    val living = componentMapper<LivingComponent>()
    val action = componentMapper<EntityActionComponent>()
    val container = componentMapper<ContainerComponent>()
    val ai = componentMapper<AIComponent>()
    val questOwner = componentMapper<QuestOwnerComponent>()
    val taskPerformer = componentMapper<TaskPerformerComponent>()
    val resource = componentMapper<ResourceComponent>()
    val source = componentMapper<SourceComponent>()

    //todo remove these
    val door = componentMapper<DoorComponent>()
    val stash = componentMapper<StashComponent>()
    val player = componentMapper<PlayerComponent>()
}