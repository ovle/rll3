package com.ovle.rll3.model.template.entity.view


enum class AnimationType {
    Idle,
    Walk,
    Attack,
    TakeHit,
    Death
}

data class AnimationTemplate(
    var type: AnimationType = AnimationType.Idle,
    var frames: Array<Pair<Int, Int>> = arrayOf(),
    var frameDuration: Float = 0.25f,
    var repeat: Boolean = false,
    var alwaysPlaying: Boolean = false,  //should be play by default if no other animation is playing
    var terminal: Boolean = false  //no default after it finished
)