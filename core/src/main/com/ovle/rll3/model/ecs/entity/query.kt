package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.component.Mappers.level
import com.ovle.rll3.model.ecs.component.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.Mappers.world
import com.ovle.rll3.model.ecs.system.level.ConnectionId
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

fun hasEntityOnPosition(levelInfo: LevelInfo, position: GridPoint2, componentClass: KClass<out Component>): Boolean =
    entitiesWith(levelInfo.objects, componentClass)
    .any {
        it[Mappers.position]?.gridPosition?.equals(position) ?: false
    }

fun entitiesOnPosition(levelInfo: LevelInfo, position: GridPoint2): Collection<Entity> =
    levelInfo.objects.filter {
        it[Mappers.position]?.gridPosition?.equals(position) ?: false
    }

fun IteratingSystem.hostEntities() = this.entities
fun EntitySystem.allEntities() = this.engine.entities

//----------------------------------------------------------------------------------------------------------------------------------

fun connectionOnPosition(levelInfo: LevelInfo, position: GridPoint2) = entitiesOnPosition(levelInfo, position)
    .singleOrNull {
        it.has<LevelConnectionComponent>()
    }

fun connection(levelInfo: LevelInfo?, id: ConnectionId?) =
    entitiesWith(levelInfo?.objects
        ?: emptyList(), LevelConnectionComponent::class)
        .find { it[levelConnection]!!.id == id }

fun obstacles(levelInfo: LevelInfo) = levelInfo.objects
    .filter { it[Mappers.collision]?.active ?: false }
    .mapNotNull { it[Mappers.position]?.gridPosition }

fun EntitySystem.levelInfoNullable() = entityWith(allEntities().toList(), LevelComponent::class)?.get(level)?.level
fun EntitySystem.levelInfo() = levelInfoNullable()!!

fun EntitySystem.worldInfoNullable() = entityWith(allEntities().toList(), WorldComponent::class)?.get(world)?.world
fun EntitySystem.worldInfo() = worldInfoNullable()!!

fun EntitySystem.playerInteractionInfo() = entityWith(allEntities().toList(), PlayerInteractionComponent::class)
    ?.get(Mappers.playerInteraction)

fun levelDescription(levelDescriptionId: LevelDescriptionId, worldInfo: WorldInfo) =
    worldInfo.levels.single { it.id == levelDescriptionId }