plugins {
  kotlin("jvm") version "1.9.23"
  id("org.jetbrains.dokka") version "1.9.20"
  `maven-publish`
}

group = "org.veupathdb.vdi"
version = "1.0.2"
description = "JSON processing library for VDI projects"

repositories {
  mavenCentral()
}

dependencies {
  api("com.fasterxml.jackson.core:jackson-core:2.17.0")
  api("com.fasterxml.jackson.core:jackson-databind:2.17.0")
  api("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
  api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
  api("com.fasterxml.jackson.module:jackson-module-parameter-names:2.17.0")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.17.0")
}

kotlin {
  jvmToolchain(18)
}

java {
  withSourcesJar()
}

tasks.dokkaHtml {
  outputDirectory.set(file("docs/dokka/${project.version}"))
}

val javadocJar = tasks.register<Jar>("javadocJar") {
  dependsOn(tasks.dokkaHtml)
  archiveClassifier.set("javadoc")
  from(file("docs/dokka/${project.version}"))
}

tasks.withType<Jar> {
  enabled = true
}

publishing {
  repositories {
    maven {
      name = "GitHub"
      url = uri("https://maven.pkg.github.com/VEuPathDB/vdi-component-json")
      credentials {
        username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
      }
    }
  }

  publications {

    create<MavenPublication>("gpr") {
      from(components["java"])

      artifact(javadocJar)

      pom {
        name.set("vdi-component-json")
        description.set(project.description)
        url.set("https://github.com/VEuPathDB/vdi-component-json")

        licenses {
          license {
            name.set("Apache-2.0")
          }
        }

        developers {
          developer {
            id.set("epharper")
            name.set("Elizabeth Paige Harper")
            email.set("foxcapades.io@gmail.com")
            url.set("https://github.com/foxcapades")
          }
        }

        scm {
          connection.set("scm:git:git://github.com/VEuPathDB/vdi-component-json.git")
          developerConnection.set("scm:git:ssh://github.com/VEuPathDB/vdi-component-json.git")
          url.set("https://github.com/VEuPathDB/vdi-component-json")
        }
      }
    }
  }
}

