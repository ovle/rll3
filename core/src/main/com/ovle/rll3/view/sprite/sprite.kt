package com.ovle.rll3.view.sprite

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PlayerInteractionComponent
import com.ovle.rll3.model.ecs.has
import com.ovle.rll3.view.layer.TextureRegions


//todo use config
fun sprite(regions: TextureRegions, x: Int, y: Int): Sprite {
    return Sprite(
        region = regions[y][x] //switched
    )
}

fun sprite(entity: Entity, regions: TextureRegions): Sprite {
    return when {
        //todo hack, will use the template
        entity.has(MoveComponent::class) -> sprite(
            regions,
            1, 1
        )
        entity.has(LightComponent::class) -> sprite(
            regions,
            0, 4
        )
        entity.has(PlayerInteractionComponent::class) -> sprite(
            regions,
            0, 0
        )
        else -> sprite(regions, 1, 0)
    }
}

class Sprite(
    private val region: TextureRegion? = null
) {

   var flipped: Boolean = false

    fun textureRegion() = region!!

    fun flip() {
        region?.flip(true, false)
//        animations?.values?.forEach { it.flip() }
//        if (offset != null) {
//            offset.x = -offset.x
//        }
    }
}