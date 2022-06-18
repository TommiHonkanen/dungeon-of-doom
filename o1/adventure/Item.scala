package o1.adventure


trait Item {

  val name: String

  val description: String

  val kind: String

  val amount: Int

  def toString: String
}

class Weapon(val name: String, val amount: Int, val description: String) extends Item{

  val kind = "weapon"

  override def toString = this.name
}

class Heal(val name: String, val amount: Int, val description: String) extends Item{

  val kind = "heal"

  override def toString = this.name
}