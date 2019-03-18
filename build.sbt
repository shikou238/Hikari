name := "Hikari"

version := "0.0"

scalaVersion := "2.12.8"

fork  := true
javaOptions in run += "-Dorg.lwjgl.librarypath=./lib"
javaOptions in run += "-XstartOnFirstThread"

libraryDependencies += "org.joml" % "joml" % "1.9.11"

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.5"

mainClass := Some("shikou238.hikari.main.Hikari")
