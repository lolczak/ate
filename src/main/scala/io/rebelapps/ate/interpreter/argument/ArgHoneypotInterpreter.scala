package io.rebelapps.ate.interpreter.argument

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, RunTime, ShellExpr}
import io.rebelapps.ate.util.syntax.\/

object ArgHoneypotInterpreter extends Interpreter[Argument] {

  override def eval[G[_]](expr: Argument[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, List[String] \/ EvalResult] =
    State.pure(List(expr.value).asLeft)

}
