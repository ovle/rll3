package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.basic.RenderComponent
import com.ovle.rll3.model.ecs.component.special.*
import com.ovle.rll3.model.ecs.component.special.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.util.basicComponents
import com.ovle.rll3.model.ecs.component.util.stateComponents
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.template.entity.EntityTemplate


fun Engine.entity(vararg components: Component) = createEntity().apply {
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun newWorld(world: WorldInfo, engine: Engine) = engine.entity(WorldComponent(world))

fun newPlayer(player: PlayerInfo, engine: Engine) = engine.entity(PlayerComponent(player))

fun newLevel(level: LevelInfo, engine: Engine) = engine.entity(LevelComponent(level))

fun newPlayerInteraction(playerEntity: Entity?, engine: Engine): Entity? = engine.entity(
    PlayerInteractionComponent(
        controlledEntity = playerEntity
        //focusedEntity = player
    ),
    RenderComponent(),
    PositionComponent()
)

fun newConnection(position: GridPoint2, gameEngine: Engine, connectionType: LevelConnectionType, levelDescriptionId: LevelDescriptionId): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    LevelConnectionComponent(type = connectionType, levelDescriptionId = levelDescriptionId)
)

fun newTemplatedEntity(template: EntityTemplate, gameEngine: Engine): Entity {
    val components = basicComponents(template) + stateComponents(template)
    return gameEngine.entity(*components.toTypedArray())
}
