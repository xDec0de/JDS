
group = "net.codersky"
version = "1.0.0-SNAPSHOT"

plugins {
	`java-library`
	`maven-publish`
	id("com.gradleup.shadow") version "8.3.5"
}

repositories {
	maven("https://repo.codersky.net/snapshots/")
	mavenCentral()
}

dependencies {

	val jda = "5.4.0"

	compileOnly("org.jetbrains:annotations:26.0.2")
	compileOnly("net.codersky.jsky:base:1.0.0-SNAPSHOT")
	compileOnly("net.codersky.jsky:yaml:1.0.0-SNAPSHOT")
	compileOnly("net.dv8tion:JDA:$jda") {
		exclude(module="opus-java")
	}

	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testCompileOnly("org.jetbrains:annotations:26.0.2")
	compileOnly("org.jetbrains:annotations:26.0.2")
	testImplementation("net.codersky.jsky:base:1.0.0-SNAPSHOT")
	testImplementation("net.codersky.jsky:yaml:1.0.0-SNAPSHOT")
	testImplementation("net.dv8tion:JDA:$jda") {
		exclude(module="opus-java")
	}
}

tasks {
	test {
		useJUnitPlatform()
	}

	named("assemble") {
		dependsOn("shadowJar")
	}

	register<Jar>("sourcesJar") {
		archiveClassifier.set("sources")
		from(sourceSets.main.get().allSource)
	}
}

publishing {
	repositories {
		maven {
			val snapshot = version.toString().endsWith("SNAPSHOT")
			url = uri("https://repo.codersky.net/" + if (snapshot) "snapshots" else "releases")
			name = if (snapshot) "cskSnapshots" else "cskReleases"
			credentials(PasswordCredentials::class)
			authentication {
				create<BasicAuthentication>("basic")
			}
		}
	}

	publications {
		create<MavenPublication>("maven") {
			groupId = project.group.toString()
			artifactId = project.name

			pom {
				packaging = "jar"
			}

			// Include the main JAR
			artifact(tasks["shadowJar"]) {
				classifier = ""
			}
			// Include the sources JAR
			artifact(tasks["sourcesJar"])
		}
	}
}

