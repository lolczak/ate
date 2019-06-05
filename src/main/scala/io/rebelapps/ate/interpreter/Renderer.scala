package io.rebelapps.ate.interpreter

trait Renderer[F[_]] {

  def render[G[_]](expr: F[ShellExpr[G]])(implicit G: Renderer[G]): String

}
