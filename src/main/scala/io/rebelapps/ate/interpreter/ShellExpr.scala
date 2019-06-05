package io.rebelapps.ate.interpreter

import cats.Functor
import io.rebelapps.ate.util.syntax.:<:

case class ShellExpr[F[_]](in: F[ShellExpr[F]])

object ShellExpr {

  def foldExpr[A, F[_]](algebra: F[A] => A)(expr: ShellExpr[F])(implicit F: Functor[F]): A =
    algebra(F.map(expr.in)(foldExpr(algebra)))

  def inject[F[_], G[_]](g: G[ShellExpr[F]])(implicit ev: G :<: F): ShellExpr[F] = ShellExpr[F](ev.inj(g))

}