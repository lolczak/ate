package io.rebelapps.ate.interpreter

import cats.Traverse
import cats.data.{EitherK, State}
import io.rebelapps.ate.util.data.Fix

trait Eval[F[_]] {

  def eval(exp: F[Result])(implicit F: Traverse[F]): State[RunTime, Result]

}

object Eval {

  def eval[F[_]](expr: Fix[F])(implicit E: Eval[F], T:Traverse[F]): State[RunTime, Result] =
    Fix.cataM[Result, F, State[RunTime, ?]](E.eval(_))(expr)

  implicit def coproductEval[F[_], G[_]](implicit F0: Eval[F], F1: Traverse[F],
                                         G0: Eval[G], G1: Traverse[G]): Eval[EitherK[F, G, ?]] =
    new Eval[EitherK[F, G, ?]] {
      override def eval(exp: EitherK[F, G, Result])(implicit F: Traverse[EitherK[F, G, ?]]): State[RunTime, Result] = {
        exp.run match {
          case Left(x)  => F0.eval(x)
          case Right(x) => G0.eval(x)
        }
      }
    }

}
