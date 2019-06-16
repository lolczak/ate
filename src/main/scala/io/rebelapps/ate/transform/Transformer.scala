package io.rebelapps.ate.transform

import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax._

trait Transformer[A, F[_]] {

  def transform(f: A => Fix[F]): A =|> Fix[F]

}
