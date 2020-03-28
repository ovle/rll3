package com.ovle.rll3.model.util

import com.ovle.rll3.model.template.EntityTemplate
import com.ovle.rll3.model.template.EntityTemplates
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.File


fun load(file: File): EntityTemplates {
    val constructor = Constructor(EntityTemplate::class.java)
    val yaml = Yaml(constructor)
    val inputStream = file.inputStream()
    val templates = yaml.loadAll(inputStream).map { it as EntityTemplate }
    return EntityTemplates(templates)
}