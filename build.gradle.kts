import com.github.javafaker.Faker
import groovy.lang.Closure

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "ch8n.dev.gradleplayground"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

val hello by tasks.registering {
    doLast {
        println("hello ch8n")
    }
}
hello {
    doLast {
        println("pkemonnnn")
    }
}


val upperCase by tasks.registering {
    doLast {
        val someString = "cHeTaN_GUpTa"
        println("==== Task: upper start =====")
        println("Original: $someString")
        println("Upper case: ${someString.toUpperCase()}")
        println("==== Task: upper end =====")
    }
}

val countUpperCase by tasks.registering {
    dependsOn(upperCase)
    doLast {
        val name = "cHeTaN GUpTa"
        val countUpperCase = name.countUpperCase()
        println("==== Task: countUpper start =====")
        println("upper case words = $countUpperCase")
        println("==== Task: countUpper end =====")
    }
}

tasks.named(upperCase.name) {
    dependsOn(hello, counter)
}

fun String.countUpperCase(): Int {
    var countUpperCase = 0
    this.filter { it != ' ' }
        .forEach {
            if (it.toUpperCase() == it) {
                ++countUpperCase
            }
        }
    return countUpperCase
}

val counter by tasks.registering {
    doLast {
        val countString = StringBuilder()
        repeat(10) {
            countString.append("$it ")
        }
        println(countString)
    }
}

defaultTasks(counter.get(), hello.get())
//defaultTasks(hello, counter, ":module1:fakeSubModuleName")

val nameAllTask by tasks.registering {
    doLast {
        //tasks.configureEach {}
        tasks.map { task -> "${task.project} | ${task.name} | ${task.temporaryDir}" }
            .joinToString("\n")
            .apply {
                println(this)
            }
    }
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        "classpath"(group = "com.github.javafaker", name = "javafaker", version = "1.0.2")
    }
}

tasks.register("fakeName") {
    dependsOn(":module1:fakeSubModuleName")
    doLast {
        val faker = Faker()
        println(
            """
            === task fakeName ===    
            Hi ! I'm ${faker.funnyName().name()},
            I live at ${faker.address().fullAddress()}
        """.trimIndent()
        )
    }
}

