package io.rebelapps.ate.interpreter.cmd

import cats.data.State
import io.rebelapps.ate.interpreter.{Interpreter, Result, RunTime}
import io.rebelapps.ate.util.data.Fix

object CatHoneypotInterpreter extends Interpreter[CatCmd] {
  override def eval[G[_]](expr: CatCmd[Fix[G]])(implicit G: Interpreter[G]): State[RunTime, Result] = ???
}
