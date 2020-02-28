package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.light.LightTilePosition
import com.ovle.rll3.model.util.config.LightConfig


fun newPlayerInteraction(playerEntity: Entity?, engine: Engine): Entity? = engine.entity(
    PlayerInteractionComponent(
        controlledEntity = playerEntity
        //focusedEntity = player
    ),
    RenderComponent(),
    PositionComponent()
)

fun newPlayer(engine: Engine): Entity? = engine.entity(
    PositionComponent(),
    MoveComponent(),
    SightComponent(5),
    RenderComponent(),
    AnimationComponent()
)

fun newDoor(position: GridPoint2, gameEngine: Engine): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    DoorComponent()
)

fun newLightSource(position: GridPoint2, engine: Engine, lightPositions: List<LightTilePosition>) =
    engine.entity(
        PositionComponent(floatPoint(position)),
        LightComponent(LightConfig.radius, lightPositions),
        RenderComponent(),
        AnimationComponent()
    )

fun newTrap(position: GridPoint2, gameEngine: Engine): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    TrapComponent()
)

fun newConnection(position: GridPoint2, gameEngine: Engine, connectionType: LevelConnectionType): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    LevelConnectionComponent(type = connectionType)
)