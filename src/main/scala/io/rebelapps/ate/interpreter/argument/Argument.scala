package io.rebelapps.ate.interpreter.argument

import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.:<:

case class Argument[F](value: String)

object Argument {

  def arg[F[_]](value: String)(implicit ev: Argument :<: F): Fix[F] = Fix.inject(Argument[Fix[F]](value))

  implicit val argInterpreter = ArgHoneypotInterpreter

}
