package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component

//todo these components will be replaced with state + template?

class DoorComponent(
    var closed: Boolean = true
) : Component

class CreatureComponent(
    var health: Int = 3
) : Component

class PlayerComponent : Component