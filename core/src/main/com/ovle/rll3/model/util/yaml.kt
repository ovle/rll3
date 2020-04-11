package com.ovle.rll3.model.util

import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.EntityTemplates
import com.ovle.rll3.model.template.structure.StructureTemplate
import com.ovle.rll3.model.template.structure.StructureTemplates
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File


fun loadEntityTemplates(file: File): EntityTemplates {
    val constructor = Constructor(EntityTemplate::class.java)
    val yaml = Yaml(constructor)
    val inputStream = file.inputStream()
    val templates = yaml.loadAll(inputStream).map { it as EntityTemplate }
    return EntityTemplates(templates)
}

fun loadStructureTemplates(file: File): StructureTemplates {
    val constructor = Constructor(StructureTemplate::class.java)
    val yaml = Yaml(constructor)
    val inputStream = file.inputStream()
    val templates = yaml.loadAll(inputStream).map { it as StructureTemplate }
    return StructureTemplates(templates)
}