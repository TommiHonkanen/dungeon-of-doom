package o1.adventure

import scala.util.Random



/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "Dungeon Of Doom"

  private val entrance = new Area("Dungeon Entrance", "There's no going back now, the only way home is through the dungeon.\nBe careful, they don't call it the dungeon of \"doom\" for no reason.")
  private val hallway = new Area("Hallway", "You are in a dark hallway. You can hear strange noises echoing off the walls.\nThe entrance has disappeared. Your only option is to go deeper.")
  private val diningHall = new Area("Dining Hall", "You are in a huge dining hall.\nDoesn't look like anyone has dined here for a few decades...")
  private val library = new Area("Library", "You find yourself in an eerie looking old library.\nAll the bookshelfs seem to be covered in cobweb and dust.")
  private val garden = new Area("Garden", "You seem to have found a garden in the middle of the dungeon, interesting.\nKeep your eyes open though, it doesn't seem very safe here...")
  private val catacombs = new Area("Catacombs", "You have arrived at what seems like an entrance to a thousand year old catacombs.\nThe entrace has collapsed so you can't go any further.\nYour only option is to go back.")
  private val vault = new Area("Vault", "It appears you have found the vault of the dungeon. You can see big piles of golden coins and gemstones everywhere.\nIt's no use to you now though...")
  private val pond = new Area("Pond", "You have arrived at a pond, but there's no chance you're going to get through it.\nThe only way to get to the other side is to go around.")
  private val crypts = new Area("Crypts", "You are in the dungeon crypts. You can see over a millenium old statues of dead kings and queens.\nSeems like a dead end. Better go back.")
  private val kingsQuarters = new Area("King's Quarters", "You have found the private quarters of someone that used to rule here a long time ago.\nDoesn't seem like the right direction though...")
  private val throneRoom = new Area("Throne Room", "You find yourself in a tall throne room, you can see a rusty iron throne.\nThere are little streaks of light leaking through the walls, the exit must be close!")
  private val exit = new Area("Exit", "")

  private val destination = exit

  this.entrance.setNeighbors(Vector("east" -> this.hallway))
  this.hallway.setNeighbors(Vector("north" -> this.garden, "south" -> this.diningHall))
  this.diningHall.setNeighbors(Vector("north" -> this.hallway, "east" -> this.library))
  this.library.setNeighbors(Vector("north" -> this.pond, "south" -> this.crypts, "west" -> this.diningHall))
  this.garden.setNeighbors(Vector("east" -> this.vault, "south" -> this.hallway, "west" -> this.catacombs))
  this.catacombs.setNeighbors(Vector("east" -> this.garden))
  this.vault.setNeighbors(Vector("east" -> this.throneRoom, "south" -> this.pond, "west" -> this.garden))
  this.pond.setNeighbors(Vector("north" -> this.vault, "south" -> this.library))
  this.crypts.setNeighbors(Vector("north" -> this.library))
  this.kingsQuarters.setNeighbors(Vector("south" -> this.throneRoom))
  this.throneRoom.setNeighbors(Vector("west" -> this.vault, "north" -> this.kingsQuarters))

  // Used for the playTurn() method to make sure the "All enemies defeated" message is printed only once
  def returnThroneRoom = this.throneRoom

  // Adds the exit to the map
  def addExit() = {
    this.throneRoom.neighbors += "south" -> this.exit
    println("\nAll enemies defeated!\nNow you just need to find the way out of here.")
  }


  // Areas where items or enemies can be located
  val potentialSpawnAreas = Vector(this.hallway, this.diningHall, this.library, this.garden, this.catacombs, this.vault, this.pond, this.crypts, this.kingsQuarters, this.throneRoom)

  // all possible weapons
  private val possibleWeapons = Vector(
    new Weapon("hammer", 20, "A weapon that deals 20 damage.\nCritical hit chance: 50%."),
    new Weapon("axe", 25, "A weapon that deals 25 damage.\nCritical hit chance: 33%."),
    new Weapon("sword", 30, "A weapon that deals 30 damage.\nCritical hit chance: 20%."),
    new Weapon("katana", 35, "A weapon that deals 35 damage.\nCritical hit chance: 16%."),
    new Weapon("shotgun", 40, "A weapon that deals 40 damage.\nCritical hit chance: 10%."),
  )

  // all possible heal items
  private val possibleHeals = Vector(
    new Heal("apple", 20, "A heal item that heals 20 HP."),
    new Heal("banana", 25, "A heal item that heals 25 HP."),
    new Heal("orange", 30, "A heal item that heals 30 HP."),
    new Heal("mango", 35, "A heal item that heals 35 HP."),
    new Heal("persimmon", 40, "A heal item that heals 40 HP."),
    new Heal("guava", 45, "A heal item that heals 45 HP."),
    new Heal("hamburger", 50, "A heal item that heals 50 HP."),
  )

  // all possible enemies
  private val possibleEnemies = Vector(
    new Enemy("Rogue Skeleton", 45, 20),
    new Enemy("Necromancer Zombie", 50, 30),
    new Enemy("Corrupt Demon", 55, 25),
    new Enemy("Dark Witch", 60, 35),
    new Enemy("Blood Lord", 65, 15),
    new Enemy("Tentacle Beast", 70, 10),
    new Enemy("Cave Troll", 90, 20),
  )

  // Places the weapons, heal items and enemies randomly on the map, multiple items can be in the same location but each enemy is always in a different area
  def placeItemsAndEnemies() = {

    val shuffledWeapons = Random.shuffle(this.possibleWeapons)
    val shuffledHeals = Random.shuffle(this.possibleHeals)
    val shuffledEnemies = Random.shuffle(this.possibleEnemies)

    val shuffledAreasWeapons = Random.shuffle(this.potentialSpawnAreas)
    shuffledWeapons.take(3).foreach(weapon => shuffledAreasWeapons(shuffledWeapons.indexOf(weapon)).addItem(weapon))

    val shuffledAreasHeals = Random.shuffle(this.potentialSpawnAreas)
    shuffledHeals.take(4).foreach(heal => shuffledAreasHeals(shuffledHeals.indexOf(heal)).addItem(heal))

    val shuffledAreasEnemies = Random.shuffle(this.potentialSpawnAreas)
    shuffledEnemies.take(3).foreach(enemy => shuffledAreasEnemies((shuffledEnemies.indexOf(enemy))).enemy = Some(enemy))
  }

  /** The character that the player controls in the game. */
  val player = new Player(entrance)

  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.isDead || this.player.hasQuit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You lost a bet against your friend and as a punishment you have to enter the infamous Dungeon Of Doom.\nBeware, no one who has entered the dungeon has ever been seen again...\nWill you be the first person to get out of there alive?\nRumors say the the exit will only appear once you have defeated all enemies inside the dungeon."


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "Finally... you are out of the dungeon, congratulations on being the first person to make it out of there alive!"
    else if (this.player.isDead)
      "You should've never taken that bet.\nGame over!"
    else  // game over due to player quitting
      "Game ended successfully."
  }


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown.
    * */
  def playTurn(command: String): String = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }


}

