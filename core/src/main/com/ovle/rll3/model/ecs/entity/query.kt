package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.basic.CollisionComponent
import com.ovle.rll3.model.ecs.component.basic.IdComponent
import com.ovle.rll3.model.ecs.component.special.*
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.level
import com.ovle.rll3.model.ecs.component.util.Mappers.player
import com.ovle.rll3.model.ecs.component.util.Mappers.world
import com.ovle.rll3.model.ecs.system.level.EntityId
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
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

fun EntitySystem.levelInfoNullable() = entityWith(allEntities().toList(), LevelComponent::class)?.get(level)?.level
fun EntitySystem.levelInfo() = levelInfoNullable()!!

fun levelInfo(entities: Array<Entity>) = entityWith(entities.toList(), LevelComponent::class)?.get(level)?.level

fun EntitySystem.world() = entityWith(allEntities().toList(), WorldComponent::class)
fun EntitySystem.worldInfoNullable() = world()?.get(world)?.world
fun EntitySystem.worldInfo() = worldInfoNullable()!!

fun playerInfoNullable(entities: List<Entity>) = entityWith(entities, PlayerComponent::class)?.get(player)?.player
fun playerInfo(entities: List<Entity>) = playerInfoNullable(entities)!!
fun EntitySystem.playerInfo() = playerInfo(allEntities().toList())

fun playerInteraction(entities: List<Entity>) = entityWith(entities, PlayerInteractionComponent::class)
fun playerInteractionInfo(entities: List<Entity>) = playerInteraction(entities)
    ?.get(Mappers.playerInteraction)
fun EntitySystem.playerInteractionInfo() = playerInteractionInfo(allEntities().toList())

fun EntitySystem.controlledEntity() = playerInteractionInfo()?.controlledEntity
fun EntitySystem.focusedEntity() = playerInteractionInfo()?.focusedEntity
fun EntitySystem.selectedEntity() = playerInteractionInfo()?.selectedEntity

fun levelDescription(levelDescriptionId: LevelDescriptionId, worldInfo: WorldInfo) =
    worldInfo.levels.single { it.id == levelDescriptionId }

fun EntitySystem.entity(id: EntityId) = entity(id, allEntities().toList())

fun entityNullable(id: EntityId, entities: Collection<Entity>) = entitiesWith(entities, IdComponent::class)
        .singleOrNull { it[Mappers.id]!!.id == id }
fun entity(id: EntityId, entities: Collection<Entity>) = entityNullable(id, entities)!!

//----------------------------------------------------------------------------------------------------------------------------------

fun Collection<Entity>.on(position: GridPoint2): Collection<Entity> =
    filter {
        it[Mappers.position]?.gridPosition?.equals(position) ?: false
    }

fun Collection<Entity>.anyOn(position: GridPoint2, componentClass: KClass<out Component>): Boolean =
    entitiesWith(this, componentClass)
        .any {
            it[Mappers.position]?.gridPosition?.equals(position) ?: false
        }

fun Collection<Entity>.positions() = mapNotNull { it[Mappers.position]?.gridPosition }.toSet()

fun Collection<Entity>.connection(id: String?) =
    entitiesWith(this, LevelConnectionComponent::class)
        .find { it[Mappers.levelConnection]!!.id == id }

fun Collection<Entity>.lightObstacles() = obstacles { it.passable4Light }
fun Collection<Entity>.bodyObstacles() = obstacles { it.passable4Body }
fun Collection<Entity>.obstacles(fn: (CollisionComponent)-> Boolean) =
    filter { it[Mappers.collision]?.let { c -> !fn.invoke(c) && c.active } ?: false }
        .mapNotNull { it[Mappers.position]?.gridPosition }