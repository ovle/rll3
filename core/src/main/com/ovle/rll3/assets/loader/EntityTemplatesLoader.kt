package com.ovle.rll3.assets.loader

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.template.entity.EntityTemplate
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File


data class EntityTemplates(val templates: Collection<EntityTemplate>)

class EntityTemplatesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<EntityTemplates, AssetLoaderParameters<EntityTemplates>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityTemplates>?)
        = file?.file()?.run { loadEntityTemplates(this) }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityTemplates>?): Array<AssetDescriptor<Any>>? = null

    private fun loadEntityTemplates(file: File): EntityTemplates {
        val constructor = Constructor(EntityTemplate::class.java)
        val yaml = Yaml(constructor)
        val inputStream = file.inputStream()
        val templates = yaml.loadAll(inputStream).map { it as EntityTemplate }
        return EntityTemplates(templates)
    }
}