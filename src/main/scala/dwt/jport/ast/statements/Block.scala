package dwt.jport.ast.statements

import scala.collection.JavaConversions._

import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.{ Block => JdtBlock }
import org.eclipse.jdt.core.dom.{ Statement => JdtStatement }

import dwt.jport.analyzers.JPortConverter
import dwt.jport.analyzers.VisitData
import dwt.jport.ast.AstNode
import dwt.jport.ast.Siblings

class Block(node: JdtBlock, private[jport] override val visitData: VisitData)
  extends Statement(node)
  with Siblings {

  val statements = {
    val s = node.statements.asInstanceOf[JavaList[JdtStatement]].to[Array]
    JPortConverter.convert(s).map(_.asInstanceOf[Statement]).to[Array]
  }

  val isEmpty = statements.isEmpty
  val nonEmpty = !isEmpty

  override def isMultiline = statements.length != 1

  override lazy val hasNext = {
    if (parent.isDefined && parent.get.nodeType == ASTNode.IF_STATEMENT) {
      val ifSatement = parent.get.asInstanceOf[IfStatement]
      (ifSatement.thenStatement.node == node &&
        ifSatement.elseStatement.isDefined) ||
        visitData.next.isDefined
    }
    else
      visitData.next.isDefined
  }
}
