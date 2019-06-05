package io.rebelapps.ate.interpreter

import cats.data.{EitherK, State}

trait Interpreter[F[_]] {

  def eval[G[_]](expr: F[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, EvalResult]

}

object Interpreter {

  def eval[F[_]](expr: ShellExpr[F])(implicit F: Interpreter[F]): State[RunTime, EvalResult] = F.eval(expr.in)

  implicit def coInterpreter[F[_], H[_]](implicit F0: Interpreter[F], H0: Interpreter[H]) = new Interpreter[EitherK[F, H, ?]] {
    override def eval[G[_]](expr: EitherK[F, H, ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, EvalResult] = {
      expr.run.fold(F0.eval(_), H0.eval(_))
    }
  }

}
