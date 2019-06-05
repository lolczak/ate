package io.rebelapps.ate.interpreter

case class RunTime(variables: Map[String, String] = Map.empty,
                   stdOut: List[String] = List.empty,
                   stdErr: List[String] = List.empty,
                   username: String = "root",
                   lastCmdStatusCode: Int = 0,
                   workingDir: String = "/")
