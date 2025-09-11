import Literal.Clause

object CnfUtils {

  def formulaCnfToList(formula: Formula): List[Clause] = {
    collectClauses(formula)
  }

  private def collectClauses(formula: Formula): List[Clause] = formula match {
    case And(left, right) =>
      collectClauses(left) ++ collectClauses(right)

    case singleClause =>
      List(collectLiterals(singleClause).toSet)
  }

  private def collectLiterals(clauseFormula: Formula): List[Literal] = clauseFormula match {
    case Or(left, right) =>
      collectLiterals(left) ++ collectLiterals(right)

    case v: Var =>
      List(Pos(v))

    case Neg(v @ Var(_)) =>
      List(NegLiteral(v))

    case _ =>
      throw new IllegalArgumentException(
        "Formula is not in valid CNF structure. Unexpected element inside a clause: " + clauseFormula
      )
  }
}