package io.rebelapps.ate.interpreter.argument

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, RunTime, ShellExpr}
import io.rebelapps.ate.util.syntax.\/

case class Argument[F](value: String)

object Argument {

  implicit val argInterpreter = new Interpreter[Argument] {

    override def eval[G[_]](expr: Argument[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, List[String] \/ EvalResult] =
      State.pure(List(expr.value).asLeft)

  }

}
