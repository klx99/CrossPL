package org.elastos.tools.crosspl.gradleplugin

internal class Config {
    var sourceList = listOf<String>()
    var withOnLoadFunc = true

    override fun toString(): String {
        return "Config{souceList=${sourceList}, withOnLoadFunc=${withOnLoadFunc}}"
    }
}
