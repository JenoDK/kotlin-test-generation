plugins {
    kotlin("jvm") version "1.3.10"
    id("org.jetbrains.intellij") version "0.3.12"
}

allprojects {
    group = "com.jeno"
    version = "1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

intellij {
    version = "2018.2.2"
	setPlugins("org.jetbrains.kotlin:1.3.10-release-IJ2018.2-1")
}

// TODO: Does not compile
//patchPluginXml {
//    changeNotes = """
//      Add change notes here.<br>
//      <em>most HTML tags may be used</em>"""
//}