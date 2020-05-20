package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.ecs.component.basic.CollisionComponent
import com.ovle.rll3.model.ecs.component.basic.IdComponent
import com.ovle.rll3.model.ecs.component.basic.TemplateComponent
import com.ovle.rll3.model.ecs.component.special.*
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.component.util.Mappers.level
import com.ovle.rll3.model.ecs.component.util.Mappers.levelConnection
import com.ovle.rll3.model.ecs.component.util.Mappers.player
import com.ovle.rll3.model.ecs.component.util.Mappers.template
import com.ovle.rll3.model.ecs.component.util.Mappers.world
import com.ovle.rll3.model.ecs.component.util.has
import com.ovle.rll3.model.ecs.system.level.ConnectionId
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

fun connection(levelInfo: LevelInfo?, id: String?) =
    entitiesWith(levelInfo?.objects
        ?: emptyList(), LevelConnectionComponent::class)
        .find { it[levelConnection]!!.id == id }

fun lightObstacles(levelInfo: LevelInfo) = obstacles(levelInfo) { it.passable4Light }
fun bodyObstacles(levelInfo: LevelInfo) = obstacles(levelInfo) { it.passable4Body }
fun obstacles(levelInfo: LevelInfo, fn: (CollisionComponent)-> Boolean) = levelInfo.objects
    .filter { it[Mappers.collision]?.let { c -> !fn.invoke(c) && c.active } ?: false }
    .mapNotNull { it[Mappers.position]?.gridPosition }

fun EntitySystem.levelInfoNullable() = entityWith(allEntities().toList(), LevelComponent::class)?.get(level)?.level
fun EntitySystem.levelInfo() = levelInfoNullable()!!

fun EntitySystem.worldInfoNullable() = entityWith(allEntities().toList(), WorldComponent::class)?.get(world)?.world
fun EntitySystem.worldInfo() = worldInfoNullable()!!

fun EntitySystem.playerInfoNullable() = entityWith(allEntities().toList(), PlayerComponent::class)?.get(player)?.player
fun EntitySystem.playerInfo() = playerInfoNullable()!!

fun EntitySystem.playerInteraction() = entityWith(allEntities().toList(), PlayerInteractionComponent::class)
fun EntitySystem.playerInteractionInfo() = playerInteraction()
    ?.get(Mappers.playerInteraction)

fun EntitySystem.controlledEntity() = playerInteractionInfo()?.controlledEntity
fun EntitySystem.focusedEntity() = playerInteractionInfo()?.focusedEntity
fun EntitySystem.selectedEntity() = playerInteractionInfo()?.selectedEntity

fun levelDescription(levelDescriptionId: LevelDescriptionId, worldInfo: WorldInfo) =
    worldInfo.levels.single { it.id == levelDescriptionId }

fun EntitySystem.entitiesWithTemplateName(name: String) = entitiesWith(allEntities().toList(), TemplateComponent::class)
        .filter { it[template]!!.template.name == name }

fun EntitySystem.entity(id: EntityId) = entitiesWith(allEntities().toList(), IdComponent::class)
        .single { it[Mappers.id]!!.id == id }