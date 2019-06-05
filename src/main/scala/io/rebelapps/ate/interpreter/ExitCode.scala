package io.rebelapps.ate.interpreter

sealed trait ExitCode

case object Ok              extends ExitCode
case class Error(code: Int) extends ExitCode
