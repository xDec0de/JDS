
group = "net.codersky"
version = "1.0.0-SNAPSHOT"

plugins {
	`java-library`
	id("com.gradleup.shadow") version "8.3.5"
}

repositories {
	maven("https://repo.codersky.net/releases/")
	mavenCentral()
}

dependencies {
	compileOnly("org.jetbrains:annotations:26.0.2")
	implementation("net.codersky.jsky:base:1.0.0")
	implementation("net.codersky.jsky:yaml:1.0.0")
	implementation("org.yaml:snakeyaml:2.3")
}
