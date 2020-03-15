package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.floatPoint
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.model.ecs.component.LevelConnectionComponent.LevelConnectionType
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
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
    AnimationComponent(),
    PlayerComponent()
)

fun newPlayerInteraction(playerEntity: Entity?, engine: Engine): Entity? = engine.entity(
    PlayerInteractionComponent(
        controlledEntity = playerEntity
        //focusedEntity = player
    ),
    RenderComponent(),
    PositionComponent()
)

fun newCreature(position: GridPoint2, gameEngine: Engine): Entity {
    return gameEngine.entity(
        PositionComponent(floatPoint(position)),
        RenderComponent(),
        CollisionComponent(),
        AnimationComponent(),
        CreatureComponent()
    )
}

fun newDoor(position: GridPoint2, gameEngine: Engine): Entity {
    val closed = true //todo
    return gameEngine.entity(
        PositionComponent(floatPoint(position)),
        RenderComponent(),
        CollisionComponent(active = closed),
        DoorComponent(closed = closed)
    )
}

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

fun newConnection(position: GridPoint2, gameEngine: Engine, connectionType: LevelConnectionType, levelDescriptionId: LevelDescriptionId): Entity = gameEngine.entity(
    PositionComponent(floatPoint(position)),
    LevelConnectionComponent(type = connectionType, levelDescriptionId = levelDescriptionId)
)