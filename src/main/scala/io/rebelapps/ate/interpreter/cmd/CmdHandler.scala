package io.rebelapps.ate.interpreter.cmd

import cats.Monad
import io.rebelapps.ate.util.syntax._

abstract class CmdHandler[F[_]: Monad] {

  def handler: String =|> F[Unit]

}
