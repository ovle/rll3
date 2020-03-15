package com.ovle.rll3.model.ecs.component

import com.badlogic.ashley.core.Component

//todo bad kind of component
//replace with state + template?
class DoorComponent(
    var closed: Boolean = true
) : Component