package com.ovle.rll3.model.module.entityAction

import com.ovle.rll3.Ticks
import com.ovle.rll3.model.module.core.component.BaseComponent

/**
 * @property current       action (will be started when [timeLeft] expires)
 * @property animation     action animation (will be started immediately)
 * @property timeLeft      time for action to be performed
 */
class EntityActionComponent(
    var current: (() -> Unit)? = null,
//    var animation: AnimationType? = null,
    var timeLeft: Ticks? = null
) : BaseComponent