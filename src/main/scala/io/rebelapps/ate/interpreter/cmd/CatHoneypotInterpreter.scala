package io.rebelapps.ate.interpreter.cmd

import cats.data.State
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, RunTime}
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.\/

object CatHoneypotInterpreter extends Interpreter[CatCmd]{
  override def eval[G[_]](expr: CatCmd[Fix[G]])(implicit G: Interpreter[G]): State[RunTime, \/[List[String], EvalResult]] = ???
}
