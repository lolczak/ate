package io.rebelapps.ate.util

object syntax {

  type =|>[-A, +B] = PartialFunction[A, B]

  type \/[+A, +B] = Either[A, B]

}
