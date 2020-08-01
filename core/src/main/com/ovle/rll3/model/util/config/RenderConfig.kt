package com.ovle.rll3.model.util.config

import com.badlogic.gdx.math.Vector3


object RenderConfig {
    lateinit var unproject: ((Vector3) -> Vector3)
    lateinit var project: ((Vector3) -> Vector3)
}