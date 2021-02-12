package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.collision.CollisionComponent
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.carrier
import com.ovle.rll3.model.module.core.component.ComponentMappers.collision
import com.ovle.rll3.model.module.core.component.ComponentMappers.core
import com.ovle.rll3.model.module.core.component.ComponentMappers.game
import com.ovle.rll3.model.module.core.component.ComponentMappers.resource
import com.ovle.rll3.model.module.core.component.ComponentMappers.tasks
import com.ovle.rll3.model.module.entityAction.EntityActionComponent
import com.ovle.rll3.model.module.entityAction.EntityActionSystem
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.gathering.ResourceType
import com.ovle.rll3.model.module.health.HealthComponent
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rll3.model.module.render.RenderComponent
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass


//todo use families?

fun entitiesWith(entities: Collection<Entity>, componentClass: KClass<out Component>) =
    ComponentMapper.getFor(componentClass.java)
    .run {
        entities.filter {
            it.has(this) && it[core]!!.isExists
        }
    }

fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).singleOrNull()
fun EntitySystem.allEntities() = this.engine.allEntities()
fun Engine.allEntities() = entities.filter { it[core]!!.isExists }

//----------------------------------------------------------------------------------------------------------------------------------

fun EntitySystem.game() = entityWith(allEntities().toList(), GameComponent::class)
fun Engine.game() = entityWith(allEntities().toList(), GameComponent::class)

fun EntitySystem.tasksInfo() = game()?.get(tasks)
fun EntitySystem.gameInfo() = game()?.get(game)

fun EntitySystem.locationInfoNullable() = gameInfo()?.location
fun EntitySystem.locationInfo() = locationInfoNullable()!!
fun locationInfo(entities: Array<Entity>) = entityWith(entities.toList(), GameComponent::class)?.get(game)?.location

fun EntitySystem.livingEntities() = entitiesWith(allEntities().toList(), HealthComponent::class)
fun EntitySystem.actionEntities() = entitiesWith(allEntities().toList(), EntityActionComponent::class)
fun EntitySystem.renderEntities() = entitiesWith(allEntities().toList(), RenderComponent::class)

//todo not entity?
fun playerInteraction(entities: List<Entity>) = entityWith(entities, PlayerInteractionComponent::class)
fun playerInteractionInfo(entities: List<Entity>) = playerInteraction(entities)
    ?.get(ComponentMappers.playerInteraction)
fun Engine.playerInteraction() = playerInteraction(allEntities().toList())
fun EntitySystem.playerInteractionInfo() = playerInteractionInfo(allEntities().toList())
fun EntitySystem.focusedEntity() = playerInteractionInfo()?.focusedEntity
fun EntitySystem.selectedEntity() = playerInteractionInfo()?.selectedEntity

fun EntitySystem.controlledEntities() = entitiesWith(allEntities().toList(), TaskPerformerComponent::class)
fun EntitySystem.entity(id: EntityId) = entity(id, allEntities().toList())
fun EntitySystem.entityNullable(id: EntityId) = entityNullable(id, allEntities().toList())

fun entityNullable(id: EntityId, entities: Collection<Entity>) = entitiesWith(entities, CoreComponent::class)
        .singleOrNull { it[core]!!.id == id }
fun entity(id: EntityId, entities: Collection<Entity>) = entityNullable(id, entities)!!

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