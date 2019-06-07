package io.rebelapps.ate.interpreter

sealed trait Result {

  def fold[A](f: EvalResult => A)(g: RunResult => A): A =
    this match {
      case e: EvalResult => f(e)
      case r: RunResult  => g(r)
    }

}

case class EvalResult(results: List[String]) extends Result

case class RunResult(output: Vector[Output] = Vector.empty,
                      exitCode: ExitCode = Ok) extends Result {

  lazy val outputStr =
    if (output.isEmpty) ""
    else {
      output.tail.foldLeft(output.head.line) {
        case (str, StdOut(line)) => str + "\n" + line
        case (str, StdErr(line)) => str + "\n" + line
      }
    }

  def putLn(line: String): RunResult = this.copy(output :+ StdOut(line))

}

sealed abstract class Output(val line: String)

case class StdOut(override val line: String) extends Output(line)
case class StdErr(override val line: String) extends Output(line)

