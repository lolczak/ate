package io.rebelapps.ate.interpreter

case class EvalResult(output: Vector[Output] = Vector.empty,
                      exitCode: ExitCode = Ok) {

  lazy val outputStr =
    if (output.isEmpty) ""
    else {
      output.tail.foldLeft(output.head.line) {
        case (str, StdOut(line)) => str + "\n" + line
        case (str, StdErr(line)) => str + "\n" + line
      }
    }

  def putLn(line: String): EvalResult = this.copy(output :+ StdOut(line))

}

sealed abstract class Output(val line: String)

case class StdOut(override val line: String) extends Output(line)
case class StdErr(override val line: String) extends Output(line)

