package com.ovle.rll3

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.model.module.ai.aiModule
import com.ovle.rll3.model.module.collision.collisionModule
import com.ovle.rll3.model.module.container.containerModule
import com.ovle.rll3.model.module.controls.controlsModule
import com.ovle.rll3.model.module.core.component.EntityComponent
import com.ovle.rll3.model.module.core.component.GlobalComponent
import com.ovle.rll3.model.module.entityAction.entityActionModule
import com.ovle.rll3.model.module.game.gameModule
import com.ovle.rll3.model.module.interaction.interactionModule
import com.ovle.rll3.model.module.life.lifeModule
import com.ovle.rll3.model.module.light.lightModule
import com.ovle.rll3.model.module.perception.perceptionModule
import com.ovle.rll3.model.module.render.renderModule
import com.ovle.rll3.model.module.resource.resourceModule
import com.ovle.rll3.model.module.skill.skillModule
import com.ovle.rll3.model.module.space.spaceModule
import com.ovle.rll3.model.module.task.taskModule
import com.ovle.rll3.model.module.tile.tileModule
import com.ovle.rll3.model.module.time.timeModule
import com.ovle.rll3.screen.screensModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.setBinding

val di = DI {
    import(screensModule)

    bind() from setBinding<EntitySystem>()
    bind() from setBinding<EntityComponent>()
    bind() from setBinding<GlobalComponent>()

    import(gameModule)

    import(aiModule)
    import(collisionModule)
    import(containerModule)
    import(controlsModule)
    import(entityActionModule)
    import(interactionModule)
    import(lifeModule)
    import(lightModule)
    import(perceptionModule)
    import(renderModule)
    import(resourceModule)
    import(skillModule)
    import(spaceModule)
    import(taskModule)
    import(tileModule)
    import(timeModule)
}
