package io.rebelapps.ate.interpreter

case class RunTime(variables: Map[String, String] = Map.empty,
                   username: String = "root",
                   lastCmd: Result = RunResult(),
                   workingDir: String = "/")
