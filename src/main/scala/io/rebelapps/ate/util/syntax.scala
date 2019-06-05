package io.rebelapps.ate.util

import cats.InjectK

object syntax {

  type =|>[-A, +B] = PartialFunction[A, B]

  type \/[+A, +B] = Either[A, B]

  type :<:[F[_], G[_]] = InjectK[F, G]

}
