package com.ovle.rll3

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.model.module.ai.aiModule
import com.ovle.rll3.model.module.controls.controlsModule
import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.model.module.entityAction.entityActionModule
import com.ovle.rll3.model.module.game.gameModule
import com.ovle.rll3.model.module.interaction.interactionModule
import com.ovle.rll3.model.module.render.renderModule
import com.ovle.rll3.model.module.resource.resourceModule
import com.ovle.rll3.model.module.skill.skillModule
import com.ovle.rll3.model.module.task.taskModule
import com.ovle.rll3.model.module.template.templateModule
import com.ovle.rll3.model.module.tile.tileModule
import com.ovle.rll3.model.module.time.timeModule
import com.ovle.rll3.screen.screensModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.setBinding

val di = DI {
    import(screensModule)

    bind() from setBinding<EntitySystem>()
    bind() from setBinding<BaseComponent>()

    //todo tagged (set?) bindings?
    import(gameModule)
    import(aiModule)
    import(renderModule)
    import(controlsModule)
    import(entityActionModule)
    import(interactionModule)
    import(resourceModule)
    import(skillModule)
    import(taskModule)
    import(templateModule)
    import(tileModule)
    import(timeModule)
}

//            bind() from setBinding<String>()
//            bind<String>().inSet() with singleton { "a" }
//            bind<String>().inSet() with singleton { "b" }
//        val b: Collection<String> by di.instance()

