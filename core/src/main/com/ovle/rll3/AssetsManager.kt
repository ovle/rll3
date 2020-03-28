package com.ovle.rll3

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.ovle.rll3.model.template.EntityTemplates
import com.ovle.rll3.model.template.EntityTemplatesLoader
import com.ovle.rll3.view.entityTemplatePath
import com.ovle.rll3.view.spriteTexturePath
import com.ovle.rll3.view.tileTexturePath

class AssetsManager(val assets: AssetManager): Disposable {

    lateinit var levelTexture: Texture
    lateinit var objectsTexture: Texture

    lateinit var templates: EntityTemplates

    init {
        val fileHandleResolver = InternalFileHandleResolver()
        assets.setLoader(Texture::class.java, TextureLoader(fileHandleResolver))
        assets.setLoader(EntityTemplates::class.java, EntityTemplatesLoader(fileHandleResolver))
    }

    fun load() {
        assets.load(tileTexturePath, Texture::class.java)
        assets.load(spriteTexturePath, Texture::class.java)
        assets.load(entityTemplatePath, EntityTemplates::class.java)

        levelTexture = assets.finishLoadingAsset<Texture>(tileTexturePath)
        objectsTexture = assets.finishLoadingAsset<Texture>(spriteTexturePath)
        templates = assets.finishLoadingAsset<EntityTemplates>(entityTemplatePath)
    }

    override fun dispose() {
        levelTexture.dispose()
        objectsTexture.dispose()
    }
}