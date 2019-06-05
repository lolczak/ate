package io.rebelapps.ate.interpreter.argument

import cats.data.State
import io.rebelapps.ate.interpreter
import io.rebelapps.ate.interpreter.{ExitCode, Interpreter, ShellExpr}

case class Argument[F](value: String)

object Argument {

  implicit val argInterpreter = new Interpreter[Argument] {

    override def eval[G[_]](expr: Argument[ShellExpr[G]])(implicit G: Interpreter[G]): State[interpreter.RunTime, String] =
      State.pure(expr.value)

  }

}
