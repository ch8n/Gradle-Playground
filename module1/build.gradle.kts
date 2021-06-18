import com.github.javafaker.Faker

plugins {
    kotlin("jvm")
}

group = "ch8n.dev.gradleplayground"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.register("fakeSubModuleName"){
    doLast {
        val faker = Faker()
        println(
            """
            === task fakeSubModuleName ===    
            Hi ! I'm ${faker.funnyName().name()},
            I live at ${faker.address().fullAddress()}
        """.trimIndent()
        )
    }
}