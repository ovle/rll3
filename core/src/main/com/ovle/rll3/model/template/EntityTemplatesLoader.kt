package com.ovle.rll3.model.template

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array


class EntityTemplatesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<EntityTemplates, AssetLoaderParameters<EntityTemplates>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityTemplates>?)
        = com.ovle.rll3.model.util.load(file!!.file())

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityTemplates>?): Array<AssetDescriptor<Any>>? = null
}