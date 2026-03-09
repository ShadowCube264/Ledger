plugins {
    kotlin("jvm") version "2.3.0"
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT"
}

var release = false
val props = properties

val modId: String by project
val modName: String by project
val modVersion: String by project
val mavenGroup: String by project

version = "1.2.8"
group = mavenGroup

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.nucleoid.xyz/")
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
    //mavenLocal()
}

dependencies {
    // To change the versions see the libs.versions.toml

    // Fabric
    minecraft(libs.minecraft)
    mappings("net.fabricmc:yarn:1.20.1+build.10:v2")
    modImplementation(libs.fabric.loader)

    // Fabric API
    modImplementation(libs.fabric.api)

    // Permissions
    modImplementation(libs.fabric.permissions)
    include(libs.fabric.permissions)

    // Translations
    modImplementation(libs.translations)
    include(libs.translations)

    // Kotlin
    modImplementation(libs.fabric.kotlin)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.sqlite.jdbc)

    include(libs.exposed.core)
    include(libs.exposed.dao)
    include(libs.exposed.jdbc)
    include(libs.exposed.java.time)
    include(libs.sqlite.jdbc)

    // Config
    implementation(libs.konf.core)
    implementation(libs.konf.toml)

    include(libs.konf.core)
    include(libs.konf.toml)
    include("com.fasterxml.jackson.module:jackson-module-kotlin:2.21.+")
    include("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.+")
    include("com.moandjiezana.toml:toml4j:0.7.2")
    include("org.apache.commons:commons-text:1.15.0")
}

tasks {

    processResources {
        inputs.property("id", modId)
        inputs.property("name", modName)
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "id" to modId,
                    "version" to version,
                    "name" to modName,
                    "fabricApi" to libs.versions.fabric.api.get(),
                    "fabricKotlin" to libs.versions.fabric.kotlin.get(),
                )
            )
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.release.set(17)
    }

    kotlin {
        jvmToolchain(17)
    }

    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        withSourcesJar()
    }
    /* 
    withType<TaskModrinthUpload> {
        onlyIf { System.getenv().contains("MODRINTH_TOKEN") }
    }
    */
}

/*
detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
    config = rootProject.files("detekt.yml")
}

gitHooks {
    setHooks(
        mapOf("pre-commit" to "detekt")
    )
}

fun getVersionMetadata(): String {
    if (release) return ""

    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    // CI builds only
    if (buildId != null) {
        return "+build.$buildId"
    }

    // No tracking information could be found about the build
    return ""
}
*/