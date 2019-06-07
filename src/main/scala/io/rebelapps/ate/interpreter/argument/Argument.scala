package io.rebelapps.ate.interpreter.argument

import io.rebelapps.ate.interpreter.ShellExpr
import io.rebelapps.ate.util.syntax.:<:

case class Argument[F](value: String)

object Argument {

  def arg[F[_]](value: String)(implicit ev: Argument :<: F): ShellExpr[F] = ShellExpr.inject(Argument[ShellExpr[F]](value))

  implicit val argInterpreter = ArgHoneypotInterpreter

}
