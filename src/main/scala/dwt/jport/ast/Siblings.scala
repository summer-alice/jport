package dwt.jport.ast

import dwt.jport.analyzers.VisitData

trait Siblings {
  type NodeType <: AstNode[_]

  protected def visitData: VisitData[NodeType]

  val hasNext = visitData.next.isDefined
  val hasPrev = visitData.prev.isDefined

  val next = visitData.next
  val prev = visitData.prev
}
