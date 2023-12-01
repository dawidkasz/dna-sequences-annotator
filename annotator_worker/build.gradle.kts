plugins {
    id("java")
}

group = "com.annotator"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0") // SLF4J binding for Log4j
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {

    manifest {
        attributes["Main-Class"] = "com.annotator.Main"
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
