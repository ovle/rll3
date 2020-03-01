package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.component.LightTilePosition
import com.ovle.rll3.model.util.config.LightConfig


fun Engine.entity(vararg components: Component) = createEntity().apply {
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun newWorld(world: WorldInfo, engine: Engine) = engine.entity(WorldComponent(world))

fun newLevel(level: LevelInfo, engine: Engine) = engine.entity(LevelComponent(level))

fun newPlayer(engine: Engine): Entity? = engine.entity(
    PositionComponent(),
    MoveComponent(),
    SightComponent(5),
    RenderComponent(),
    AnimationComponent()
)

fun newPlayerInteraction(playerEntity: Entity?, engine: Engine): Entity? = engine.entity(
    PlayerInteractionComponent(
        controlledEntity = playerEntity
        //focusedEntity = player
    ),
    RenderComponent(),
    PositionComponent()
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

fun newTrigger(position: GridPoint2, gameEngine: Engine): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    TriggerComponent()
)

fun newConnection(position: GridPoint2, gameEngine: Engine, connectionType: LevelConnectionType): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    LevelConnectionComponent(type = connectionType)
)