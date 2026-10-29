// What every node works out the same way, whichever loader builds it. Applied by the three
// buildscripts after they set `extra["apdGame"]` to the Minecraft version of the node:
// the Java level, the mixin list, the access widener and the access transformer.
val game = extra["apdGame"] as String

// "26.1" against "1.21.11": numeric, part by part, a missing part being zero
fun atLeast(version: String): Boolean {
    val a = game.split(".").map { it.toInt() }
    val b = version.split(".").map { it.toInt() }
    for (i in 0 until maxOf(a.size, b.size)) {
        val x = a.getOrElse(i) { 0 }
        val y = b.getOrElse(i) { 0 }
        if (x != y) return x > y
    }
    return true
}
fun atMost(version: String): Boolean = !atLeast(version) || game == version

extra["apdJava"] = if (atLeast("26.1")) JavaVersion.VERSION_25 else JavaVersion.VERSION_21

// Client mixins this version has something to hook into, written into the mixin config
extra["apdClientMixins"] = buildList {
    // The paper doll is drawn through a picture-in-picture renderer from 1.21.6
    if (atLeast("26.1")) add("hud.GuiGraphicsExtractorMixin") else if (atLeast("1.21.6")) add("hud.GuiGraphicsMixin")
    // Locks the boat's yaw through its render state; from 26.1 PaperDollRenderer sets it on the state itself
    if (atLeast("1.21.2") && !atLeast("26.1")) add("patch.AbstractBoatRendererMixin")
    // Culling fix for the doll's buffer source (upstream left it out on 1.21.5)
    if (!atLeast("26.1") && (!atLeast("1.21.5") || atLeast("1.21.6"))) add("patch.BufferSourceMixin")
    add("retexture.AbstractButtonMixin")
    add(if (atLeast("1.21.4")) "retexture.AbstractScrollAreaMixin" else "retexture.AbstractSelectionListMixin")
    add("retexture.AbstractSliderButtonMixin")
    add("retexture.EditBoxMixin")
}.joinToString(",\n        ") { "\"$it\"" }

// The blocks of the access widener's source file that exist on this version: a block whose
// first lines are "# @since X" and/or "# @until X" is kept only on the versions it names,
// both ends included. Blocks are separated by a blank line.
val widenerBlocks: List<String> = run {
    val since = Regex("""^# @since (\S+)""")
    val until = Regex("""^# @until (\S+)""")
    rootProject.file("src/main/accesswidener/ayame-paperdoll.accesswidener").readText()
        .split(Regex("\n\\s*\n")).filter { block ->
            val lines = block.lines()
            val from = lines.firstNotNullOfOrNull { since.find(it)?.groupValues?.get(1) }
            val to = lines.firstNotNullOfOrNull { until.find(it)?.groupValues?.get(1) }
            (from == null || atLeast(from)) && (to == null || atMost(to))
        }
}

// The access widener Loom reads: named namespace on obfuscated versions, official from 26.1
run {
    val file = layout.buildDirectory.file("generated/accesswidener/ayame-paperdoll.accesswidener").get().asFile
    file.parentFile.mkdirs()
    file.writeText(widenerBlocks.joinToString("\n\n")
        .replace("\${namespace}", if (atLeast("26.1")) "official" else "named").trimEnd() + "\n")
    extra["apdAccessWidener"] = file
}

// The same thing as the access transformer NeoForge and Forge read, in Mojang's names
run {
    val file = layout.buildDirectory.file("generated/accesstransformer/META-INF/accesstransformer.cfg").get().asFile
    val lines = widenerBlocks.flatMap { it.lines() }.mapNotNull { line ->
        val part = line.trim().split(" ")
        fun name(internal: String) = internal.replace('/', '.')
        when {
            part.size == 3 && part[0] == "accessible" && part[1] == "class" -> "public ${name(part[2])}"
            part.size == 5 && part[0] == "accessible" && part[1] == "field" -> "public ${name(part[2])} ${part[3]}"
            part.size == 5 && part[0] == "accessible" && part[1] == "method" -> "public ${name(part[2])} ${part[3]}${part[4]}"
            else -> null
        }
    }
    file.parentFile.mkdirs()
    file.writeText(lines.joinToString("\n") + "\n")
    extra["apdAccessTransformer"] = file
}
