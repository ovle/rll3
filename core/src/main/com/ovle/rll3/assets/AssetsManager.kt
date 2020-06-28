package com.ovle.rll3.assets

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.assets.loader.*
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.view.*


class AssetsManager(val assets: AssetManager): Disposable {

    lateinit var levelTexture: Texture
    lateinit var objectsTexture: Texture
    lateinit var guiTexture: Texture

    private val entityTemplates = mutableMapOf<TemplatesType, EntityTemplates>()
    private val entityViewTemplates = mutableMapOf<TemplatesType, EntityViewTemplates>()
    private val structureTemplates = mutableMapOf<TemplatesType, StructureTemplates>()
    val behaviorTrees = mutableMapOf<String, BehaviorTree<EntityBlackboard>>()

    init {
        val fileHandleResolver = InternalFileHandleResolver()

        with(assets) {
            setLoader(Texture::class.java, TextureLoader(fileHandleResolver))
            setLoader(EntityTemplates::class.java, EntityTemplatesLoader(fileHandleResolver))
            setLoader(EntityViewTemplates::class.java, EntityViewTemplatesLoader(fileHandleResolver))
            setLoader(StructureTemplates::class.java, StructureTemplatesLoader(fileHandleResolver))
            setLoader(BehaviorTrees::class.java, BehaviorTreesLoader(fileHandleResolver))
        }
    }

    fun load() {
        with(assets) {
            load(tileTexturePath, Texture::class.java)
            load(spriteTexturePath, Texture::class.java)
            load(guiTexturePath, Texture::class.java)
            load(behaviorTreePath, BehaviorTrees::class.java)
        }

        TemplatesType.values().forEach {
            var path = "$entityTemplatePath${it.value}.yaml"
            assets.load(path, EntityTemplates::class.java)
            entityTemplates[it] = assets.finishLoadingAsset(path)

            path = "$entityViewTemplatePath${it.value}.yaml"
            assets.load(path, EntityViewTemplates::class.java)
            entityViewTemplates[it] = assets.finishLoadingAsset(path)

            path = "$structureTemplatePath${it.value}.yaml"
            assets.load(path, StructureTemplates::class.java)
            structureTemplates[it] = assets.finishLoadingAsset(path)
        }

        levelTexture = assets.finishLoadingAsset(tileTexturePath)
        objectsTexture = assets.finishLoadingAsset(spriteTexturePath)
        guiTexture = assets.finishLoadingAsset(guiTexturePath)

        val trees = assets.finishLoadingAsset<BehaviorTrees>(behaviorTreePath)
        trees.trees.forEach {
            behaviorTrees[it.name] = it.tree
        }

        TemplatesRegistry.entityTemplates = entityTemplates
        TemplatesRegistry.entityViewTemplates = entityViewTemplates
        TemplatesRegistry.structureTemplates = structureTemplates
    }

    override fun dispose() {
        levelTexture.dispose()
        objectsTexture.dispose()
        guiTexture.dispose()
    }
}