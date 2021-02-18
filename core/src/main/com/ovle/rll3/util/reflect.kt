package com.ovle.rll3.util

import io.github.classgraph.ClassGraph

fun loadedClasses(pkg: String) = ClassGraph().acceptPackages(pkg).scan()