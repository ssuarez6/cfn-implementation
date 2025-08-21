sealed trait Literal {
  def variable: Var
  override def toString(): String = this match {
    case Pos(v) => v.name
    case NegLiteral(v) => s"-${v.name}"
  }
}

case class Pos(variable: Var) extends Literal
case class NegLiteral(variable: Var) extends Literal

object Literal {
  implicit class CFNSyntax(clauses: List[List[Literal]]) {
    def asString: String = {
      clauses.map{ disjunctions => 
        s"(${disjunctions.mkString(" || ")})"
      }.mkString(" & ")
    }
  }
}
