package io.rebelapps.ate.interpreter.argument

import io.rebelapps.ate.interpreter.argument.Argument.arg
import io.rebelapps.ate.parsing.{ShellExpr, SimpleArgumentAst}
import io.rebelapps.ate.transform.Transformer
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.{:<:, =|>}

class ArgTransformer[F[_]](implicit ev: Argument :<: F) extends Transformer[ShellExpr, F] {

  override def transform(f: ShellExpr => Fix[F]): =|>[ShellExpr, Fix[F]] = {
    case SimpleArgumentAst(value) => arg[F](value)
  }

}
