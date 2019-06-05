package io.rebelapps.ate.interpreter

import cats.data.State

trait Interpreter[F[_]] {

  def eval[G[_]](expr: F[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, String]

}

object Interpreter {

  def eval[F[_]](expr: ShellExpr[F])(implicit F: Interpreter[F]): State[RunTime, String] = F.eval(expr.in)

}
