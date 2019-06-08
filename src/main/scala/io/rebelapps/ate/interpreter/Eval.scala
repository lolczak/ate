package io.rebelapps.ate.interpreter

import cats.data.EitherK
import cats.mtl.MonadState
import cats.{Monad, Traverse}
import io.rebelapps.ate.util.data.Fix

trait Eval[F[_]] {

  def eval[G[_]](exp: F[Result])(implicit F: Traverse[F], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result]

}

object Eval {

  def eval[F[_], G[_]](expr: Fix[F])
                      (implicit E: Eval[F], T: Traverse[F], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result] =
    Fix.cataM[Result, F, G](E.eval(_))(expr)

  implicit def coproductEval[F[_], G[_]](implicit F0: Eval[F],
                                         F1: Traverse[F],
                                         G0: Eval[G],
                                         G1: Traverse[G]): Eval[EitherK[F, G, ?]] =
    new Eval[EitherK[F, G, ?]] {
      override def eval[H[_]](exp: EitherK[F, G, Result])
                             (implicit F: Traverse[EitherK[F, G, ?]], H0: MonadState[H, RunTime], H1: Monad[H]): H[Result] =
        exp.run.fold(F0.eval[H](_), G0.eval[H](_))
    }

}
