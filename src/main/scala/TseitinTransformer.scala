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

  def encode(f: Formula): List[List[Literal]] = {
    val (rootLiteral, clauses) = toCfn(f)
    List(rootLiteral) +: clauses
  }

  def toCfn(f: Formula): (Literal, List[List[Literal]]) = {
    f match {

      case v: Var => (Pos(v), List.empty)

      case Neg(v: Var) => (NegLiteral(v), List.empty)

      case And(left, right) => 
        val (leftLiteral, leftClauses) = toCfn(left)
        val (rightLiteral, rightClauses) = toCfn(right)
        val p = freshVar()
        val pLit = Pos(p)

        val newClauses = List(
          List(negate(pLit), leftLiteral),
          List(negate(pLit), rightLiteral),
          List(pLit, negate(leftLiteral), negate(rightLiteral))
        )

        (pLit, leftClauses ++ rightClauses ++ newClauses)

      case Or(left, right) =>
        val (leftLiteral, leftClauses) = toCfn(left)
        val (rightLiteral, rightClauses) = toCfn(right)
        val p = freshVar()
        val pLit = Pos(p)

        val newClauses = List(
          List(pLit, negate(leftLiteral)),
          List(pLit, negate(rightLiteral)),
          List(negate(pLit), leftLiteral, rightLiteral)
        )

        (pLit, leftClauses ++ rightClauses ++ newClauses)

      case Impl(left, right) => 
        toCfn(Or(Neg(left), right))

      case Equiv(left, right) =>
        val (leftLiteral, leftClauses) = toCfn(left)
        val (rightLiteral, rightClauses) = toCfn(right)

        val p = freshVar()
        val pLiteral = Pos(p)

        val newClauses = List(
          List(negate(pLiteral), negate(leftLiteral), rightLiteral),
          List(negate(pLiteral), leftLiteral, negate(rightLiteral)),
          List(pLiteral, negate(leftLiteral), rightLiteral),
          List(pLiteral, leftLiteral, negate(rightLiteral))
        )

        (pLiteral, leftClauses ++ rightClauses ++ newClauses)

      case Neg(subFormula) =>
        val (subLiteral, subClauses) = toCfn(subFormula)

        val p = freshVar()
        val pLiteral = Pos(p)

        val newClauses = List(
          List(negate(pLiteral), negate(subLiteral)),
          List(pLiteral, subLiteral)
        )

        (pLiteral, subClauses ++ newClauses)
    }
  }


}
