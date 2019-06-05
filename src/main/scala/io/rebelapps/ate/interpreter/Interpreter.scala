package io.rebelapps.ate.interpreter

import cats.data.{EitherK, State}
import io.rebelapps.ate.util.syntax._

trait Interpreter[F[_]] {

  def eval[G[_]](expr: F[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, List[String] \/ EvalResult]

}

object Interpreter {

  def eval[F[_]](expr: ShellExpr[F])(implicit F: Interpreter[F]): State[RunTime, List[String] \/ EvalResult] = F.eval(expr.in)

  implicit def coInterpreter[F[_], H[_]](implicit F0: Interpreter[F], H0: Interpreter[H]) = new Interpreter[EitherK[F, H, ?]] {
    override def eval[G[_]](expr: EitherK[F, H, ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, List[String] \/ EvalResult] = {
      expr.run.fold(F0.eval(_), H0.eval(_))
    }
  }

}
