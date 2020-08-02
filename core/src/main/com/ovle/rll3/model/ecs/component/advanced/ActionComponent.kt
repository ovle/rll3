package com.ovle.rll3.model.ecs.component.advanced

import com.ovle.rll3.Ticks
import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.template.entity.view.AnimationType

/**
 * @property current       action (will be started when [timeLeft] expires)
 * @property animation     action animation (will be started immediately)
 * @property timeLeft      time for action to be performed
 */
class ActionComponent(
    var current: (() -> Unit)? = null,
    var animation: AnimationType? = null,
    var timeLeft: Ticks? = null
) : BaseComponent