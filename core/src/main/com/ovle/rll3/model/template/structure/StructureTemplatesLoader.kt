package com.ovle.rll3.model.template.structure

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File


data class StructureTemplates(val templates: Collection<StructureTemplate>)

class StructureTemplatesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<StructureTemplates, AssetLoaderParameters<StructureTemplates>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<StructureTemplates>?)
        = file?.file()?.run { loadStructureTemplates(this) }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<StructureTemplates>?): Array<AssetDescriptor<Any>>? = null

    private fun loadStructureTemplates(file: File): StructureTemplates {
        val constructor = Constructor(StructureTemplate::class.java)
        val yaml = Yaml(constructor)
        val inputStream = file.inputStream()
        val templates = yaml.loadAll(inputStream).map { it as StructureTemplate }
        return StructureTemplates(templates)
    }
}