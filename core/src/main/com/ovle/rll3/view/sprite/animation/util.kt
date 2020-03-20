package com.ovle.rll3.view.sprite.animation

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.CreatureComponent
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PlayerComponent
import com.ovle.rll3.model.ecs.component.has
import com.ovle.rll3.view.layer.TextureRegions


//todo hack, will use template instead marker components
fun animations(entity: Entity, regions: TextureRegions): Array<FrameAnimation> {
    return when {
        entity.has<CreatureComponent>() -> arrayOf(
            FrameAnimation(regions, scelAnimationIdle),
            FrameAnimation(regions, scelAnimationDeath),
            FrameAnimation(regions, scelAnimationDamaged)
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