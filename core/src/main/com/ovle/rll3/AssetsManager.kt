package com.ovle.rll3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.model.template.EntityTemplates
import com.ovle.rll3.model.template.EntityTemplatesLoader
import com.ovle.rll3.model.template.EntityTemplatesType
import com.ovle.rll3.view.entityTemplatePath
import com.ovle.rll3.view.spriteTexturePath
import com.ovle.rll3.view.tileTexturePath

class AssetsManager(val assets: AssetManager): Disposable {

    lateinit var levelTexture: Texture
    lateinit var objectsTexture: Texture

    val templates = mutableMapOf<EntityTemplatesType, EntityTemplates>()

    init {
        val fileHandleResolver = InternalFileHandleResolver()
        assets.setLoader(Texture::class.java, TextureLoader(fileHandleResolver))
        assets.setLoader(EntityTemplates::class.java, EntityTemplatesLoader(fileHandleResolver))
    }

    fun load() {
        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)

        EntityTemplatesType.values().forEach {
            val path = "$entityTemplatePath${it.value}.yaml"
            assets.load(path, EntityTemplates::class.java)
            templates[it] = assets.finishLoadingAsset<EntityTemplates>(path)
        }

        levelTexture = assets.finishLoadingAsset<Texture>(tileTexturePath)
        objectsTexture = assets.finishLoadingAsset<Texture>(spriteTexturePath)
    }

    override fun dispose() {
        levelTexture.dispose()
        objectsTexture.dispose()
    }
}