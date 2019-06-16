package io.rebelapps.ate.interpreter.cmd.dir

import io.rebelapps.ate.interpreter.cmd.dir.DirCmd.ls
import io.rebelapps.ate.parsing.{Cmd, ShellExpr}
import io.rebelapps.ate.transform.Transformer
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.{:<:, =|>}

class DirTransformer[F[_]](implicit ev: DirCmd :<: F) extends Transformer[ShellExpr, F] {

  override def transform(f: ShellExpr => Fix[F]): ShellExpr =|> Fix[F] = {
    case Cmd("ls", args) => ls[F](args.map(f))
  }

}
