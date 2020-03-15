package com.ovle.rll3.view.sprite.animation

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.CreatureComponent
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PlayerComponent
import com.ovle.rll3.model.ecs.component.has
import com.ovle.rll3.view.layer.TextureRegions

data class AnimationTemplate(
    val id: String,
    val frames: Array<Pair<Int, Int>>,
    val frameDuration: Float = 0.125f,
    val repeat: Boolean = false,
    val alwaysPlaying: Boolean = false  //should be play by default if no other animation is playing
)

//todo hack, will use template instead marker components
fun animations(entity: Entity, regions: TextureRegions): Array<FrameAnimation> {
    return when {
        entity.has<CreatureComponent>() -> arrayOf(
            FrameAnimation(regions, scelAnimationIdle)
        )
        entity.has<PlayerComponent>() -> arrayOf(
            FrameAnimation(regions, wizAnimationIdle),
            FrameAnimation(regions, wizAnimationWalk)
        )
        entity.has<LightComponent>() -> arrayOf(
            FrameAnimation(regions, torchAnimation)
        )
        else -> arrayOf()
    }
}

class FrameAnimation(
    regions: TextureRegions,
    val template: AnimationTemplate
) {
    private val frames: Array<TextureRegion?> = framesFromRegions(regions, template.frames)
    private var animation: Animation<TextureRegion?> = Animation(template.frameDuration, *frames)
    private var stateTime: Float = 0.0f
    private var active: Boolean = false

    private fun framesFromRegions(regions: Array<Array<TextureRegion>>, frames: Array<Pair<Int, Int>>): Array<TextureRegion?> {
        return frames.map {
            (x, y) -> regions[y][x] //switched
        }.toTypedArray()
    }

    fun currentFrame(deltaTime: Float): TextureRegion? {
        stateTime += deltaTime
        return animation.getKeyFrame(stateTime, template.repeat);
    }

    fun start() {
        active = true
    }

    fun stop() {
        active = false
        stateTime = 0.0f
    }

    fun flip() {
        frames.forEach { it?.flip(true, false) }
    }
}