import Literal.Clause

object DPLL {

  def solve(clauses: List[Clause]): Boolean = dpllRecursive(clauses, Map.empty)

  def dpllRecursive(clauses: List[Clause], assignment: Map[Var, Boolean]): Boolean = {
    val (simplifiedClauses, newAssignment) = simplify(clauses, assignment)
    if (simplifiedClauses.isEmpty) {
      return true
    }

    if (simplifiedClauses.exists(_.isEmpty)) {
      return false
    }

    val variableToGuess = simplifiedClauses.head.head.variable
    dpllRecursive(simplifiedClauses, newAssignment + (variableToGuess -> true)) ||
    dpllRecursive(simplifiedClauses, newAssignment + (variableToGuess -> false))
  }

  def simplify(clauses: List[Clause], assignment: Map[Var, Boolean]): (List[Clause], Map[Var, Boolean]) = {
    findUnitClause(clauses) match {
      case Some(unitLiteral) =>
        val (newClauses, newAssignment) = applyAssignment(clauses, assignment, unitLiteral)
        return simplify(newClauses, newAssignment)
      case _ =>
    }
    findPureLiteral(clauses, assignment) match {
      case Some(pureLiteral) =>
        val (newClauses, newAssignment) = applyAssignment(clauses, assignment, pureLiteral)
        return simplify(newClauses, newAssignment)
      case _ =>
    }
    (clauses, assignment)
  }

  def findUnitClause(clauses: List[Clause]): Option[Literal] = clauses.find(_.size == 1).map(_.head)

  def findPureLiteral(clauses: List[Clause], assignment: Map[Var, Boolean]): Option[Literal] = {
    val allLiterals = clauses.flatten
    val allVars = allLiterals.map(_.variable).toSet
    val unassignedVars = allVars.filterNot(assignment.contains)
    unassignedVars.map { v =>
      val hasPos = allLiterals.contains(Pos(v))
      val hasNeg = allLiterals.contains(Neg(v))
      if (hasPos && !hasNeg) Some(Pos(v))
      else if (!hasPos && hasNeg) Some(NegLiteral(v))
      else None
    }
      .find(_.isDefined)
      .flatten
  }

  def applyAssignment(clauses: List[Clause], assignment: Map[Var, Boolean], literal: Literal): (List[Clause], Map[Var, Boolean]) = {
    val (variable, value) = literal match {
      case Pos(v) => (v, true)
      case NegLiteral(v) => (v, false)
    }
    val literalComplement = literal match {
      case Pos(v) => NegLiteral(v)
      case NegLiteral(v) => Pos(v)
    }
    val newClauses = clauses
      .filterNot(_.contains(literal))
      .map(clause => clause - literalComplement)
    (newClauses, assignment + (variable -> value))
  }
}
