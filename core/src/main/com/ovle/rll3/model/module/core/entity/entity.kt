package com.ovle.rll3.model.module.core.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.ovle.rll3.EntityId
import com.ovle.rll3.model.module.core.component.IdComponent
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.game.PlayerInfo
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.game.PlayerComponent
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rll3.model.module.core.component.basicComponents
import com.ovle.rll3.model.module.core.component.stateComponents
import com.ovle.rll3.model.template.entity.EntityTemplate
import java.util.*

fun Engine.entity(id: EntityId, vararg components: Component) = createEntity().apply {
    this.add(IdComponent(id))
    components.forEach { component -> this.add(component) }
    addEntity(this)
}

fun randomId() = UUID.randomUUID().toString()

fun newPlayer(player: PlayerInfo, engine: Engine) = engine.entity(player.playerId, PlayerComponent(player))

fun newLocation(location: LocationInfo, engine: Engine) = engine.entity(location.id, GameComponent(location))

fun newPlayerInteraction(engine: Engine): Entity? = engine.entity(
    "not used",
    PlayerInteractionComponent(),
    PositionComponent()
)

fun newTemplatedEntity(id: EntityId, template: EntityTemplate, gameEngine: Engine): Entity {
    val components = basicComponents(template) + stateComponents(template)
    return gameEngine.entity(id, *components.toTypedArray())
}
