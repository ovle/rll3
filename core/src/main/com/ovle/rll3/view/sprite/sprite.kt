package com.ovle.rll3.view.sprite

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.LightComponent
import com.ovle.rll3.model.ecs.component.PlayerControlledComponent
import com.ovle.rll3.model.ecs.has
import com.ovle.rll3.view.layer.TextureRegions


//todo use config
fun sprite(regions: TextureRegions, x: Int, y: Int): Sprite {
    return Sprite(
        region = regions[y][x] //switched
    )
}

//todo use config
//todo animation support
fun sprite(entity: Entity, regions: TextureRegions): Sprite {
    return when {
        entity.has(PlayerControlledComponent::class) -> sprite(
            regions,
            1, 1
        )
        entity.has(LightComponent::class) -> sprite(
            regions,
            0, 4
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