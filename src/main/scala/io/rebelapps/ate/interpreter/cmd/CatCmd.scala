package io.rebelapps.ate.interpreter.cmd

import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.:<:

case class CatCmd[F](args: List[F])

object CatCmd {

  def cat[F[_]](args: List[Fix[F]])(implicit ev: CatCmd :<: F): Fix[F] = Fix.inject(CatCmd(args))

}