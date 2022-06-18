package o1.adventure

// Class for the enemies
class Enemy(val name: String, var HP: Int, val damage: Int) {
  override def toString = this.name
}
