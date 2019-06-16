package io.rebelapps.ate.interpreter

import io.rebelapps.ate.interpreter.argument.ArgInterpreter
import io.rebelapps.ate.interpreter.cmd.cat.CatHoneypotInterpreter
import io.rebelapps.ate.interpreter.cmd.dir.DirHoneypotInterpreter

trait HoneypotInstances {

  implicit val dirInterpreter = DirHoneypotInterpreter

  implicit val catInterpreter = CatHoneypotInterpreter

  implicit val argInterpreter = ArgInterpreter

}
