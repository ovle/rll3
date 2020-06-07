package com.ovle.rll3.assets.loader

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.template.entity.view.EntityViewTemplate
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File


data class EntityViewTemplates(val templates: Collection<EntityViewTemplate>)

class EntityViewTemplatesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<EntityViewTemplates, AssetLoaderParameters<EntityViewTemplates>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityViewTemplates>?)
        = file?.file()?.run { loadEntityViewTemplates(this) }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<EntityViewTemplates>?): Array<AssetDescriptor<Any>>? = null

    private fun loadEntityViewTemplates(file: File): EntityViewTemplates {
        val constructor = Constructor(EntityViewTemplate::class.java)
        val yaml = Yaml(constructor)
        val inputStream = file.inputStream()
        val templates = yaml.loadAll(inputStream).map { it as EntityViewTemplate }
        return EntityViewTemplates(templates)
    }
}