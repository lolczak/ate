package io.rebelapps.ate.interpreter

import cats.data.State

trait Eval[F[_]] {

  def eval(exp: F[EvalResult]): State[RunTime, EvalResult]

}
