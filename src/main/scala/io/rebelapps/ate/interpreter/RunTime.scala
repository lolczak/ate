package io.rebelapps.ate.interpreter

case class RunTime(variables: Map[String, String] = Map.empty,
                   username: String = "root",
                   lastCmd: EvalResult = EvalResult(),
                   workingDir: String = "/")
