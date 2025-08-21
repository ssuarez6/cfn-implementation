import scala.util.parsing.combinator._
import scala.util.matching.Regex

object FormulaParser extends RegexParsers {

  override protected val whiteSpace: Regex = """\s*""".r

  def wrap[T](p: Parser[T]): Parser[T] = p <~ whiteSpace

  val variable: Parser[Var] = wrap("[a-zA-Z]+".r ^^ { name => Var(name) })

  val negOp: Parser[String]   = wrap("-")
  val andOp: Parser[String]   = wrap("&")
  val orOp: Parser[String]    = wrap("||")
  val implOp: Parser[String]  = wrap("->")
  val equivOp: Parser[String] = wrap("<->")
  val lParen: Parser[String]  = wrap("(")
  val rParen: Parser[String]  = wrap(")")

  def formula: Parser[Formula] = equiv

  def equiv: Parser[Formula] = chainl1(impl, equivOp ^^^ { (f1: Formula, f2: Formula) => Equiv(f1, f2) })

  def impl: Parser[Formula] = chainl1(or, implOp ^^^ { (f1: Formula, f2: Formula) => Impl(f1, f2) })

  def or: Parser[Formula] = chainl1(and, orOp ^^^ { (f1: Formula, f2: Formula) => Or(f1, f2) })

  def and: Parser[Formula] = chainl1(factor, andOp ^^^ { (f1: Formula, f2: Formula) => And(f1, f2) })

  def factor: Parser[Formula] =
    variable |
      (negOp ~> factor ^^ { f => Neg(f) }) |
      (lParen ~> formula <~ rParen)

  def apply(input: String): Either[String, Formula] = {
    parse(phrase(whiteSpace ~> formula), input) match {
      case Success(result, _) => Right(result)
      case NoSuccess(msg, _)  => Left(s"Could not parse due to: $msg")
      case Error(msg, _)      => Left(s"Could not parse due to: $msg")
      case Failure(msg, _)    => Left(s"Could not parse due to: $msg")
    }
  }
}
