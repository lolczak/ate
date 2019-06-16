package io.rebelapps.ate.interpreter.argument

import cats.{Monad, Traverse}
import cats.mtl.MonadState
import io.rebelapps.ate.interpreter.{Eval, EvalResult, Result, RunTime}

object ArgInterpreter extends Eval[Argument]{

  override def eval[G[_]](exp: Argument[Result])
                         (implicit F: Traverse[Argument], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result] =
    G1.point(EvalResult(List(exp.value)))

}
