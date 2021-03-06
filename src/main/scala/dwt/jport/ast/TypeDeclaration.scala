package dwt.jport.ast

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.{ BodyDeclaration => JdtBodyDeclaration }
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.{ Type => JdtType }
import org.eclipse.jdt.core.dom.{ TypeDeclaration => JdtTypeDeclaration }

import dwt.jport.Symbol
import dwt.jport.Type
import dwt.jport.analyzers.VisitData

class TypeDeclaration(node: JdtTypeDeclaration, private[jport] override val visitData: VisitData)
  extends AbstractTypeDeclaration(node)
  with TypeParameters
  with Siblings {

  import Type.fullyQualfiedName

  val bodyDeclarations = node.bodyDeclarations.asInstanceOf[JavaList[JdtBodyDeclaration]]
  val isInterface = node.isInterface
  val unescapedName = node.getName.getIdentifier
  val name = Symbol.translate(unescapedName)

  lazy val isLocal =
    parent.map(_.nodeType != ASTNode.COMPILATION_UNIT).getOrElse(false)

  private lazy val binding = node.resolveBinding
  private val base = Option(binding.getSuperclass)
  private val isJavaLangObject = base.map(_.getQualifiedName).exists(_ == "java.lang.Object")

  val imports: Array[String] = {
    val superImport = base.map(fullyQualfiedName(_))
    val interfaceImports = binding.getInterfaces.map(fullyQualfiedName(_))

    if (isJavaLangObject) interfaceImports else interfaceImports ++ superImport
  }

  val superclass = if (isJavaLangObject) None else Symbol.translate(base.map(_.getName))

  val interfaces = node.superInterfaceTypes.
    map(e => nameOfType(e.asInstanceOf[JdtType]))

  val hasMembers = !node.bodyDeclarations.isEmpty

  override def isMultiline = true

  protected override def typeParametersBinding = binding.getTypeParameters

  private def nameOfType(typ: JdtType): String = {
    if (typ == null) return null

    if (typ.isSimpleType)
      return Symbol.translate(typ.asInstanceOf[SimpleType].getName.getFullyQualifiedName)

    else null
  }
}
