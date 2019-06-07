package io.rebelapps.ate.interpreter

import io.rebelapps.ate.util.data.Fix

trait Renderer[F[_]] {

  def render[G[_]](expr: F[Fix[G]])(implicit G: Renderer[G]): String

}
