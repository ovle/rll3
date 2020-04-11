package com.ovle.rll3.model.template.structure

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.util.loadStructureTemplates


class StructureTemplatesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<StructureTemplates, AssetLoaderParameters<StructureTemplates>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<StructureTemplates>?)
        = file?.file()?.run { loadStructureTemplates(this) }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<StructureTemplates>?): Array<AssetDescriptor<Any>>? = null
}