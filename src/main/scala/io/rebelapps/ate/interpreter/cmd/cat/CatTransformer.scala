package io.rebelapps.ate.interpreter.cmd.cat

import io.rebelapps.ate.parsing.{Cmd, ShellExpr}
import io.rebelapps.ate.transform.Transformer
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.{:<:, =|>}

class CatTransformer[F[_]](implicit ev: CatCmd :<: F) extends Transformer[ShellExpr, F] {

  override def transform(f: ShellExpr => Fix[F]): =|>[ShellExpr, Fix[F]] = {
    case Cmd(name, args) => CatCmd.cat[F](args.map(f))
  }

}
