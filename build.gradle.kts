import com.github.javafaker.Faker
import java.time.Duration.ofMillis


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
        classpath("com.github.javafaker:javafaker:1.0.2")
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


// ---------- lazy task dependencies -----------
val taskX by tasks.registering {
    doLast {
        println("taskX")
    }
}

// Using a Gradle Provider
taskX {
    dependsOn(provider {
        tasks.filter { task -> task.name.startsWith("lib") }
    })

}

val lib1 by tasks.registering {
    doLast {
        println("i'm task lib1")
    }
}

val lib2: Task by tasks.creating {
    doLast {
        println("i'm task lib2")
    }
}

val unrelatedLibTask: Task by tasks.creating {
    doLast {
        println("i'm should be loaded")
    }
}

// ----------- ordering task execution --------

val taskA by tasks.registering {
    doLast {
        println("taskA")
    }
}
val taskB by tasks.registering {
    doLast {
        println("taskB")
    }
}
taskA {
    mustRunAfter(taskB)
}

val taskC by tasks.registering {
    doLast {
        println("taskC")
    }
}

taskC {
    shouldRunAfter(taskA)
}

// ------------ Skippable task ----------

hello {
    onlyIf {
        !project.hasProperty("skipHello")
    }
}

// ---------- stop execution task -------
val taskA1 by tasks.registering {
    doLast {
        println("taskA1")
    }
}

val taskB1 by tasks.registering {
    dependsOn(taskA1)
    doLast {
        println("taskB1")
    }
}

taskA1 {
    doFirst {
        throw StopExecutionException()
    }
}

// ----------- disabled task ---------

val disabledTask by tasks.registering {
    enabled = false
    doLast {
        println("I'm disabled task!")
    }
}

val enabledTask by tasks.registering {
    dependsOn(disabledTask)
    doLast {
        println("I'm enabled task!")
    }
}

// -------- Time out task ---------

val timeOutTask by tasks.registering {
    doLast {
        Thread.sleep(1000)
    }
    timeout.set(ofMillis(500))
}

val timeTestTask by tasks.registering {
    dependsOn(timeOutTask)
    doLast {
        println("I'm testing time out!")
    }
}

