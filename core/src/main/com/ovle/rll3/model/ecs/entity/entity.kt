package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.basic.IdComponent
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.basic.RenderComponent
import com.ovle.rll3.model.ecs.component.special.*
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.util.basicComponents
import com.ovle.rll3.model.ecs.component.util.stateComponents
import com.ovle.rll3.model.ecs.system.level.EntityId
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.template.entity.EntityTemplate
import java.util.*

fun Engine.entity(id: EntityId, vararg components: Component) = createEntity().apply {
    this.add(IdComponent(id))
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun randomId() = UUID.randomUUID().toString()

fun newWorld(world: WorldInfo, engine: Engine) = engine.entity(world.id, WorldComponent(world, TimeInfo()))

fun newPlayer(player: PlayerInfo, engine: Engine) = engine.entity(player.playerId, PlayerComponent(player))

fun newLevel(level: LevelInfo, engine: Engine) = engine.entity(level.id, LevelComponent(level))

fun newPlayerInteraction(playerEntity: Entity?, engine: Engine): Entity? = engine.entity(
    "not used",
    PlayerInteractionComponent(
        controlledEntity = playerEntity,
        focusedEntity = playerEntity
    ),
    PositionComponent()
)

fun newConnection(id: EntityId, position: GridPoint2, gameEngine: Engine, connectionType: LevelConnectionType, levelDescriptionId: LevelDescriptionId): Entity {
    return gameEngine.entity(
        id,
        PositionComponent(position),
        LevelConnectionComponent(id, type = connectionType, levelDescriptionId = levelDescriptionId)
    )
}

fun newTemplatedEntity(id: EntityId, template: EntityTemplate, gameEngine: Engine): Entity {
    val components = basicComponents(template) + stateComponents(template)
    return gameEngine.entity(id, *components.toTypedArray())
}
