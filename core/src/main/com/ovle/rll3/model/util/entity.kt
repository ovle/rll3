package com.ovle.rll3.model.util

//todo modules
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.collision.CollisionComponent
import com.ovle.rll3.model.module.collision.Components.collision
import com.ovle.rll3.model.module.container.Components.carrier
import com.ovle.rll3.model.module.core.Components.core
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.module.core.entity.entitiesWith
import com.ovle.rll3.model.module.core.entity.entityWith
import com.ovle.rll3.model.module.entityAction.EntityActionComponent
import com.ovle.rll3.model.module.game.Components.game
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.interaction.Components.playerInteraction
import com.ovle.rll3.model.module.life.LifeComponent
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.module.render.RenderComponent
import com.ovle.rll3.model.module.resource.Components.resource
import com.ovle.rll3.model.module.resource.ResourceType
import com.ovle.rll3.model.module.space.position
import com.ovle.rll3.model.module.space.positionOrNull
import com.ovle.rll3.model.module.task.Components.tasks
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import com.ovle.rll3.model.module.time.Components.time
import ktx.ashley.get
import ktx.ashley.has

fun game(entities: Array<Entity>) = entityWith(entities.toList(), GameComponent::class)
fun Engine.game() = game(allEntities().toTypedArray())
fun Engine.tasksInfo() = game()?.get(tasks)
fun Engine.gameInfo() = game()?.get(game)
fun Engine.timeInfo() = game()?.get(time)
fun Engine.locationInfo() = gameInfo()?.location
fun locationInfo(entities: Array<Entity>) = entityWith(entities.toList(), GameComponent::class)
    ?.get(game)?.location

fun Engine.livingEntities() = entitiesWith(allEntities().toList(), LifeComponent::class)
fun Engine.actionEntities() = entitiesWith(allEntities().toList(), EntityActionComponent::class)
fun Engine.renderEntities() = entitiesWith(allEntities().toList(), RenderComponent::class)
fun Engine.aiEntities() = entitiesWith(allEntities().toList(), AIComponent::class)
fun Engine.perceptionEntities() = entitiesWith(allEntities().toList(), PerceptionComponent::class)

//todo not entity?
fun playerInteractionInfo(entities: List<Entity>) = game(entities.toTypedArray())
    ?.get(playerInteraction)

fun Engine.playerInteractionInfo() = playerInteractionInfo(allEntities().toList())
fun Engine.focusedEntity() = playerInteractionInfo()?.focusedEntity
fun Engine.selectedEntity() = playerInteractionInfo()?.selectedEntity
fun Engine.controlledEntities() = entitiesWith(allEntities().toList(), TaskPerformerComponent::class)
fun Engine.newEntity(id: EntityId) = newEntity(id, allEntities().toList())
fun Engine.entityNullable(id: EntityId) = entityNullable(id, allEntities().toList())
fun entityNullable(id: EntityId, entities: Collection<Entity>) = entitiesWith(entities, CoreComponent::class)
        .singleOrNull { it[core]!!.id == id }

fun newEntity(id: EntityId, entities: Collection<Entity>) = entityNullable(id, entities)!!

//----------------------------------------------------------------------------------------------------------------------------------
//entities
fun Collection<Entity>.on(p: GridPoint2): Collection<Entity> =
    filter { it.positionOrNull()?.equals(p) ?: false }

fun Collection<Entity>.resources(type: ResourceType? = null) = filter {
    it.has(resource) && (type == null || it[resource]!!.type == type)
}

fun Collection<Entity>.carriers() = filter { it.has(carrier) }

//components
fun Collection<Entity>.positions() = mapNotNull { it.positionOrNull() }.toSet()
fun Collection<Entity>.lightObstacles() = obstacles { it.passable4Light }
fun Collection<Entity>.bodyObstacles() = obstacles { it.passable4Body }
fun Collection<Entity>.obstacles(fn: (CollisionComponent)-> Boolean) =
    filter { it[collision]?.let { c -> !fn.invoke(c) && c.active } ?: false }
        .mapNotNull { it.position() }