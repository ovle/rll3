package com.ovle.rll3.model.module.life

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.TemplatedState
import com.ovle.rll3.model.module.core.component.EntityComponent
import org.kodein.di.*


val lifeModule = DI.Module("life") {
    bind<EntitySystem>().inSet() with singleton {
        HealthSystem()
    }
    bind<EntitySystem>().inSet() with singleton {
        HungerSystem()
    }
    bind<EntitySystem>().inSet() with singleton {
        StaminaSystem()
    }

    bind<EntityComponent>(tag = "living") with factory { value: TemplatedState? ->
        HealthComponent(
            maxHealth = value!!["health"] as Int,
            maxStamina = value["stamina"] as Int
        ).apply {
            health = maxHealth
            stamina = maxStamina
        }
    }
}