package io.rebelapps.ate.transform

import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax._

trait Transformer[A, F[_]] { self =>

  def transform(f: A => Fix[F]): A =|> Fix[F]

  def |+|(that: Transformer[A, F]): Transformer[A, F] =
    new Transformer[A, F] {

      override def transform(f: A => Fix[F]): =|>[A, Fix[F]] = self.transform(f) orElse that.transform(f)

    }

}
