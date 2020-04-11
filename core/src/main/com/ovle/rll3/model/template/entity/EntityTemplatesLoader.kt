package com.ovle.rll3.model.template.entity

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.util.loadEntityTemplates


class EntityTemplatesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<EntityTemplates, AssetLoaderParameters<EntityTemplates>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityTemplates>?)
        = file?.file()?.run { loadEntityTemplates(this) }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityTemplates>?): Array<AssetDescriptor<Any>>? = null
}