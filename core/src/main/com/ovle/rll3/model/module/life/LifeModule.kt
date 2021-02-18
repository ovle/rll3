package com.ovle.rll3.model.module.life

import com.ovle.rll3.ComponentFactory
import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class LifeModule: Module {

    override fun systems(context: Context) = listOf(
        HealthSystem(),
        HungerSystem(),
        StaminaSystem()
    )

    override fun templateComponents(name: String): List<ComponentFactory> {
        return when (name) {
            "living" -> listOf { value ->
                HealthComponent(
                    maxHealth = value!!["health"] as Int,
                    maxStamina = value["stamina"] as Int
                ).apply {
                    health = maxHealth
                    stamina = maxStamina
                }
            }
            else -> emptyList()
        }
    }
}