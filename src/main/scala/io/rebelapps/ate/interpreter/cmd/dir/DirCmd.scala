package io.rebelapps.ate.interpreter.cmd.dir

import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.:<:

sealed trait DirCmd[F]

case class LsCmd[F](args: List[F]) extends DirCmd[F]

object DirCmd {

  def ls[F[_]](args: List[Fix[F]])(implicit ev: DirCmd :<: F): Fix[F] = Fix.inject(LsCmd(args): DirCmd[Fix[F]])

  implicit val dirTraverse = DirTraverse

}
