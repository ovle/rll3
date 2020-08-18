package com.ovle.rll3.assets

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.assets.loader.*
import com.ovle.rll3.model.module.ai.BaseBlackboard
import com.ovle.rll3.model.module.skill.skillTemplates
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.view.*
import com.ovle.rll3.view.layer.TexturesInfo


class AssetsManager(val assets: AssetManager): Disposable {

    lateinit var levelTexture: TexturesInfo
    lateinit var objectsTexture: TexturesInfo
    lateinit var guiTexture: TexturesInfo

    private val entityTemplates = mutableMapOf<TemplatesType, EntityTemplates>()
    private val entityViewTemplates = mutableMapOf<TemplatesType, EntityViewTemplates>()
    private val structureTemplates = mutableMapOf<TemplatesType, StructureTemplates>()
    val behaviorTrees = mutableMapOf<String, BehaviorTree<BaseBlackboard>>()

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

        levelTexture = TexturesInfo(assets.finishLoadingAsset(tileTexturePath))
        objectsTexture = TexturesInfo(assets.finishLoadingAsset(spriteTexturePath))
        guiTexture = TexturesInfo(assets.finishLoadingAsset(guiTexturePath))

        val trees = assets.finishLoadingAsset<BehaviorTrees>(behaviorTreePath)
        trees.trees.forEach {
            behaviorTrees[it.name] = it.tree
        }

        TemplatesRegistry.entityTemplates = entityTemplates
        TemplatesRegistry.entityViewTemplates = entityViewTemplates
        TemplatesRegistry.structureTemplates = structureTemplates
        TemplatesRegistry.skillTemplates = skillTemplates().associateBy { it.name } //todo
    }

    override fun dispose() {
        levelTexture.all.forEach { it.dispose() }
        objectsTexture.all.forEach { it.dispose() }
        guiTexture.all.forEach{ it.dispose() }
    }
}