import Literal.Clause

class TseitinTransformer {

  private var nextVarId = 0;
  private def freshVar(): Var = {
    nextVarId += 1
    Var(s"p$nextVarId")
  }

  def negate(lit: Literal): Literal = lit match {
    case Pos(v)        => NegLiteral(v)
    case NegLiteral(v) => Pos(v)
  }

  def encode(f: Formula): List[Clause] = {
    val (rootLiteral, clauses) = toCfn(f)
    List(Set(rootLiteral)) ++ clauses
  }

  def toCfn(f: Formula): (Literal, List[Clause]) = {
    f match {

      case v: Var => (Pos(v), List.empty)

      case Neg(v: Var) => (NegLiteral(v), List.empty)

      case And(left, right) =>
        val (leftLiteral, leftClauses)   = toCfn(left)
        val (rightLiteral, rightClauses) = toCfn(right)
        val p                            = freshVar()
        val pLit                         = Pos(p)

        val newClauses = List(
          Set(negate(pLit), leftLiteral),
          Set(negate(pLit), rightLiteral),
          Set(pLit, negate(leftLiteral), negate(rightLiteral))
        )

        (pLit, leftClauses ++ rightClauses ++ newClauses)

      case Or(left, right) =>
        val (leftLiteral, leftClauses)   = toCfn(left)
        val (rightLiteral, rightClauses) = toCfn(right)
        val p                            = freshVar()
        val pLit                         = Pos(p)

        val newClauses = List(
          Set(pLit, negate(leftLiteral)),
          Set(pLit, negate(rightLiteral)),
          Set(negate(pLit), leftLiteral, rightLiteral)
        )

        (pLit, leftClauses ++ rightClauses ++ newClauses)

      case Impl(left, right) =>
        toCfn(Or(Neg(left), right))

      case Equiv(left, right) =>
        val (leftLiteral, leftClauses)   = toCfn(left)
        val (rightLiteral, rightClauses) = toCfn(right)

        val p        = freshVar()
        val pLiteral = Pos(p)

        val newClauses = List(
          Set(negate(pLiteral), negate(leftLiteral), rightLiteral),
          Set(negate(pLiteral), leftLiteral, negate(rightLiteral)),
          Set(pLiteral, negate(leftLiteral), rightLiteral),
          Set(pLiteral, leftLiteral, negate(rightLiteral))
        )

        (pLiteral, leftClauses ++ rightClauses ++ newClauses)

      case Neg(subFormula) =>
        val (subLiteral, subClauses) = toCfn(subFormula)

        val p        = freshVar()
        val pLiteral = Pos(p)

        val newClauses = List(
          Set(negate(pLiteral), negate(subLiteral)),
          Set(pLiteral, subLiteral)
        )

        (pLiteral, subClauses ++ newClauses)
    }
  }

}
