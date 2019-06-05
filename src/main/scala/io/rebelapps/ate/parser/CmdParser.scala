package io.rebelapps.ate.parser

import io.rebelapps.ate.ast._

import scala.language.implicitConversions
import scala.util.parsing.combinator.{PackratParsers, Parsers}
import scala.util.parsing.input.CharArrayReader

object CmdParser extends Parsers with PackratParsers {

  type Elem = Char

  def parse(cmdSource: String): Either[String, ExpAst] = {
    parseGrammar(new CharArrayReader(cmdSource.toCharArray)) match {
      case Success(exp, next) => Right(exp)
      case err: NoSuccess     => Left(err.toString)
    }
  }

  implicit def charParser(char: Char): Parser[Char] = elem(s"$char", ch => if (ch.isLetter) ch.toLower == char.toLower else ch == char)

  implicit def stringParser(chars: String): Parser[String] =
    chars.toCharArray.toList.map(charParser).foldLeft(success("")) {
      case (acc, parser) => acc ~ parser ^^ { case h ~ t => h + t }
    }

  lazy val eot: Parser[Unit] =
    Parser { in =>
      if (in.atEnd) Success((), in)
      else Failure("no end of input", in)
    }

  lazy val parseGrammar: Parser[ExpAst] = shellExp <~ endOfExp

  lazy val shellExp: Parser[ExpAst] = compoundCmd | singleCmd

  lazy val compoundCmd: Parser[ExpAst] =
    (singleCmd <~ opt(whitespace)) ~ (combinator <~ opt(whitespace)) ~ shellExp ^^ {
      case exp1 ~ "|"  ~ exp2 => PipeCombinator(exp1, exp2)
      case exp1 ~ "||" ~ exp2 => OrCombinator(exp1, exp2)
      case exp1 ~ "&&" ~ exp2 => AndCombinator(exp1, exp2)
      case exp1 ~ ";"  ~ exp2 => ThenCombinator(exp1, exp2)
    }

  lazy val combinator: Parser[String] = "||" | "&&" | ";" | "|"

  lazy val singleCmd: Parser[Cmd] = cmdName ~ opt(whitespace ~> arguments) ^^ { case name ~ maybeArgs => Cmd(name, maybeArgs.getOrElse(List.empty): _*) }

  lazy val endOfExp = (opt(whitespace) ~ (eot  | (";" ~ eot))).named("end of cmd")

  lazy val cmdName = rep1(elem("any char", ch => (ch >= 33 && ch <=126 && ch != ';' && ch != '&' && ch != '|') || ch.isLetter || ch == '_' || ch == '-' )) ^^ { chars => chars.mkString }

  lazy val argument: Parser[Argument] = (not(combinator) ~> (doubleQuotedArg | singleQuotedArg | cmdSubstitutionExp | simpleArg)).named("arg")

  lazy val doubleQuotedArg: Parser[Argument] = '\"' ~> rep1("\\\"" | elem("not dbl qt", _ != '\"')) <~ '\"' ^^ { chars => DoubleQuoted(chars.mkString) }

  lazy val singleQuotedArg: Parser[Argument] = '\'' ~> rep1("\\\'" | elem("not sngl qt", _ != '\'')) <~ '\'' ^^ { chars => SingleQuoted(chars.mkString) }

  lazy val cmdSubstitutionExp: Parser[Argument] =
    for {
      expString <- cmdSubstitution | backQuotesCmdSubstitution
      cmdExp    <- Parser { in =>
        parseGrammar(new CharArrayReader(expString.toCharArray)) match {
          case s@ Success(s1,_) => Success(s1, in)
          case e => e
        }
      }
    } yield CmdSubstitution(cmdExp)

  lazy val cmdSubstitution: Parser[String] = "$(" ~> rep1(elem("not )", _ != ')')) <~ ')' ^^ { chars => chars.mkString }

  lazy val backQuotesCmdSubstitution: Parser[String] = '`' ~> rep1("\\`" | elem("not sngl qt", _ != '`')) <~ '`' ^^ { chars => chars.mkString }

  lazy val simpleArg: Parser[Argument] = rep1(not(combinator) ~> printable) ^^ { chars => SimpleArgument(chars.mkString) } named "simple arg"

  lazy val arguments: Parser[List[Argument]] = separatedSequence(argument, whitespace, endOfArgs).named("args")

  lazy val endOfArgs = (opt(whitespace) ~ guard(eot | combinator)).named("end of args")

  lazy val whitespace: Parser[Unit] = (rep1(elem("whitespace", _.isWhitespace) | elem("No-Break Space", _ == 0xA0)) ^^^ (())).named("whitespace")

  lazy val printable: Parser[Char] = elem("any char", ch => (ch >= 33 && ch <=126) || ch.isLetter).named("printable")

  def repTill[T](p: => Parser[T], end: => Parser[Any]): Parser[List[T]] =
    end ^^^ List.empty | (p ~ repTill(p, end)) ^^ { case x ~ xs => x :: xs }

  def separatedSequence[T](p: => Parser[T], s: => Parser[Any], end: => Parser[Any]): Parser[List[T]] =
    for {
      x  <- p
      xs <- repTill(s ~> p, end)
    } yield x :: xs

}
