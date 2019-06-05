package io.rebelapps.ate.interpreter.argument

import cats.data.State
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, RunTime, ShellExpr}

case class Argument[F](value: String)

object Argument {

  implicit val argInterpreter = new Interpreter[Argument] {

    override def eval[G[_]](expr: Argument[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, EvalResult] =
      State.pure(EvalResult().putLn(expr.value))

  }

}
