package com.ovle.rll3

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.model.ecs.system.ai.BehaviorTrees
import com.ovle.rll3.model.ecs.system.ai.BehaviorTreesLoader
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.entity.EntityTemplates
import com.ovle.rll3.model.template.entity.EntityTemplatesLoader
import com.ovle.rll3.model.template.structure.StructureTemplates
import com.ovle.rll3.model.template.structure.StructureTemplatesLoader
import com.ovle.rll3.view.*

class AssetsManager(val assets: AssetManager): Disposable {

    lateinit var levelTexture: Texture
    lateinit var objectsTexture: Texture
    lateinit var guiTexture: Texture

    val entityTemplates = mutableMapOf<TemplatesType, EntityTemplates>()
    val structureTemplates = mutableMapOf<TemplatesType, StructureTemplates>()
    val behaviorTrees = mutableMapOf<String, BehaviorTrees>()

    init {
        val fileHandleResolver = InternalFileHandleResolver()
        assets.setLoader(Texture::class.java, TextureLoader(fileHandleResolver))
        assets.setLoader(EntityTemplates::class.java, EntityTemplatesLoader(fileHandleResolver))
        assets.setLoader(StructureTemplates::class.java, StructureTemplatesLoader(fileHandleResolver))
        assets.setLoader(BehaviorTrees::class.java, BehaviorTreesLoader(fileHandleResolver))
    }

    fun load() {
        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)
        assets.load(guiTexturePath, Texture::class.java)
        assets.load(behaviorTreePath, BehaviorTrees::class.java)

        TemplatesType.values().forEach {
            var path = "$entityTemplatePath${it.value}.yaml"
            assets.load(path, EntityTemplates::class.java)
            entityTemplates[it] = assets.finishLoadingAsset(path)

            path = "$structureTemplatePath${it.value}.yaml"
            assets.load(path, StructureTemplates::class.java)
            structureTemplates[it] = assets.finishLoadingAsset(path)
        }

        levelTexture = assets.finishLoadingAsset(tileTexturePath)
        objectsTexture = assets.finishLoadingAsset(spriteTexturePath)
        guiTexture = assets.finishLoadingAsset(guiTexturePath)
        //todo
        behaviorTrees["test"] = assets.finishLoadingAsset(behaviorTreePath)
    }

    override fun dispose() {
        levelTexture.dispose()
        objectsTexture.dispose()
    }
}