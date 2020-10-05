package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.collision.CollisionComponent
import com.ovle.rll3.model.module.core.component.IdComponent
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import com.ovle.rll3.model.module.core.component.ComponentMappers
import com.ovle.rll3.model.module.core.component.ComponentMappers.game
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import ktx.ashley.get
import ktx.ashley.has
import kotlin.reflect.KClass


//todo use families?

fun entitiesWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = ComponentMapper.getFor(componentClass.java)
    .run {
        entities.filter { it.has(this) }
    }

fun entityWith(entities: Collection<Entity>, componentClass: KClass<out Component>) = entitiesWith(entities, componentClass).singleOrNull()

fun IteratingSystem.hostEntities() = this.entities
fun EntitySystem.allEntities() = this.engine.entities

//----------------------------------------------------------------------------------------------------------------------------------

fun EntitySystem.gameInfo() = entityWith(allEntities().toList(), GameComponent::class)?.get(game)

fun EntitySystem.locationInfoNullable() = gameInfo()?.location
fun EntitySystem.locationInfo() = locationInfoNullable()!!
fun locationInfo(entities: Array<Entity>) = entityWith(entities.toList(), GameComponent::class)?.get(game)?.location

fun playerInteraction(entities: List<Entity>) = entityWith(entities, PlayerInteractionComponent::class)
fun playerInteractionInfo(entities: List<Entity>) = playerInteraction(entities)
    ?.get(ComponentMappers.playerInteraction)
fun EntitySystem.playerInteraction() = playerInteraction(this.allEntities().toList())
fun EntitySystem.playerInteractionInfo() = playerInteractionInfo(allEntities().toList())

fun EntitySystem.focusedEntity() = playerInteractionInfo()?.focusedEntity
fun EntitySystem.selectedEntity() = playerInteractionInfo()?.selectedEntity
//fun EntitySystem.controlledEntities() = entitiesWith(allEntities().toList(), TaskPerformerComponent::class)
fun EntitySystem.entity(id: EntityId) = entity(id, allEntities().toList())
fun EntitySystem.entityNullable(id: EntityId) = entityNullable(id, allEntities().toList())

fun entityNullable(id: EntityId, entities: Collection<Entity>) = entitiesWith(entities, IdComponent::class)
        .singleOrNull { it[ComponentMappers.id]!!.id == id }
fun entity(id: EntityId, entities: Collection<Entity>) = entityNullable(id, entities)!!

//----------------------------------------------------------------------------------------------------------------------------------

fun Collection<Entity>.on(position: GridPoint2): Collection<Entity> =
    filter {
        it[ComponentMappers.position]?.gridPosition?.equals(position) ?: false
    }

fun Collection<Entity>.anyOn(position: GridPoint2, componentClass: KClass<out Component>): Boolean =
    entitiesWith(this, componentClass)
        .any {
            it[ComponentMappers.position]?.gridPosition?.equals(position) ?: false
        }

fun Collection<Entity>.positions() = mapNotNull { it[ComponentMappers.position]?.gridPosition }.toSet()
fun Collection<Entity>.lightObstacles() = obstacles { it.passable4Light }
fun Collection<Entity>.bodyObstacles() = obstacles { it.passable4Body }
fun Collection<Entity>.obstacles(fn: (CollisionComponent)-> Boolean) =
    filter { it[ComponentMappers.collision]?.let { c -> !fn.invoke(c) && c.active } ?: false }
        .mapNotNull { it[ComponentMappers.position]?.gridPosition }