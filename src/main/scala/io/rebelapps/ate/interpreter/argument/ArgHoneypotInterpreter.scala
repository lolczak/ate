package io.rebelapps.ate.interpreter.argument

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, Result, RunTime}
import io.rebelapps.ate.util.data.Fix

object ArgHoneypotInterpreter extends Interpreter[Argument] {

  override def eval[G[_]](expr: Argument[Fix[G]])(implicit G: Interpreter[G]): State[RunTime, Result] =
    State.pure(EvalResult(List(expr.value)))

}
