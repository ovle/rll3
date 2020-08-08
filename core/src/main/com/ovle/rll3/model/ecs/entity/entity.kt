package com.ovle.rll3.model.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.ecs.component.basic.IdComponent
import com.ovle.rll3.model.ecs.component.basic.PositionComponent
import com.ovle.rll3.model.ecs.component.dto.LevelInfo
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.special.LevelComponent
import com.ovle.rll3.model.ecs.component.special.PlayerComponent
import com.ovle.rll3.model.ecs.component.special.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.component.util.basicComponents
import com.ovle.rll3.model.ecs.component.util.stateComponents
import com.ovle.rll3.model.template.entity.EntityTemplate
import java.util.*

fun Engine.entity(id: EntityId, vararg components: Component) = createEntity().apply {
    this.add(IdComponent(id))
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun randomId() = UUID.randomUUID().toString()

fun newPlayer(player: PlayerInfo, engine: Engine) = engine.entity(player.playerId, PlayerComponent(player))

fun newLevel(level: LevelInfo, engine: Engine) = engine.entity(level.id, LevelComponent(level))

fun newPlayerInteraction(engine: Engine): Entity? = engine.entity(
    "not used",
    PlayerInteractionComponent(),
    PositionComponent()
)

fun newTemplatedEntity(id: EntityId, template: EntityTemplate, gameEngine: Engine): Entity {
    val components = basicComponents(template) + stateComponents(template)
    return gameEngine.entity(id, *components.toTypedArray())
}
