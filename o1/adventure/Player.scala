package o1.adventure

import scala.collection.mutable.{Map, Buffer}

import scala.math.{min, max}

import scala.io.StdIn._

import scala.util.Random


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var HP = 100                              // Health of the player, intially 100 and maximum is also 100

  def isDead = this.HP <= 0 // Determines if the player is dead

  private var myItems: Map[String, Item] = Map()

  def get(itemName: String) = {
    if (this.location.contains(itemName)) {
      this.myItems += itemName -> this.location.removeItem(itemName).get
      "You pick up the " + itemName + ".\n" + myItems(itemName).description
    } else {
      "There is no " + itemName + " here to pick up."
    }
  }

  // Gives info about the item
  def info(itemName: String) = {
    if (this.myItems.contains(itemName)) {
      this.myItems(itemName).description
    } else {
      "You don't have that item."
    }
  }

  def drop(itemName: String) = {
    if (this.myItems.contains(itemName)) {
      this.location.addItem(this.myItems(itemName))
      this.myItems.remove(itemName)
      "You drop the " + itemName + "."
    } else {
      "You don't have that."
    }
  }

  def has(itemName: String) = this.myItems.contains(itemName)

  // Method for the use command
  def use(itemName: String) = {
    if (this.has(itemName)) {
      if (this.myItems(itemName).kind == "weapon") {
        if (this.currentLocation.enemy.nonEmpty) {
          this.engage()
        } else {
          "There is no enemy to attack here."
        }
      } else {
          this.heal(itemName)
      }
    } else {
      "You don't have that."
    }
  }

  // Method for using heal items
  private def heal(itemName: String) = {
    if (this.HP == 100) {
        "You're already at full health."
      } else {
        this.HP = min((this.HP + this.myItems(itemName).amount), 100)
        this.myItems.remove(itemName)
        "Heal item used! Your new HP: " + this.HP + "."
      }
  }

  // Handles the turn-based combat
  private def engage() = {
    println("\n\nCOMBAT" + "\n------\n")
    println("You engaged in combat with " + this.currentLocation.enemy.get.name + "!" + " It deals " + this.currentLocation.enemy.get.damage + " damage per hit.")
    println("It has " + this.currentLocation.enemy.get.HP + " HP.")
    println(this.health + "\n")

    while (this.HP > 0 && this.currentLocation.enemy.get.HP > 0) {
      println("You have: " + this.myItems.keys.mkString(" "))
      var input = readLine("What do you want to use?: ")

      while (!this.has(input) && input != "skip") {
        println("You don't have that.")
        input = readLine("What do you want to use?: ")
      }

      if (this.has(input)) {
        if (this.myItems(input).kind == "weapon") {
          // Calculates whether the attack is going to be a critical hit or not using the percentage in the weapon's description
          if (((Random.nextInt(100) + 1) % (100 / this.myItems(input).description.split(" ")(8).split("%")(0).toInt)) == 0) {
            this.currentLocation.enemy.get.HP -= (this.myItems(input).amount * 1.5).toInt
            println("Critical hit!")
            println("You dealt " + (this.myItems(input).amount * 1.5).toInt + " damage to the " + this.currentLocation.enemy.get.name + "!")
            println("It now has " + max(0, this.currentLocation.enemy.get.HP) + " HP.")
            enemyAttack()
          } else {
              this.currentLocation.enemy.get.HP -= this.myItems(input).amount
              println("You dealt " + this.myItems(input).amount + " damage to the " + this.currentLocation.enemy.get.name + "!")
              println("It now has " + max(0, this.currentLocation.enemy.get.HP) + " HP.")
              enemyAttack()
           }
        } else if (this.myItems(input).kind == "heal") {
            if (this.HP == 100) {
              println(this.heal(input))
            } else {
              println(this.heal(input))
              enemyAttack()
            }
        }
      } else if (input == "skip"){
        enemyAttack()
      }

      // Takes care of the enemy's attack
      def enemyAttack() = {
        if (this.currentLocation.enemy.get.HP > 0) {
        this.HP -= this.currentLocation.enemy.get.damage

        println("The " + this.currentLocation.enemy.get.name + " attacked you and you took " + this.currentLocation.enemy.get.damage + " damage.")
        println(this.health)
        }
      }

      println()

    }

    if (this.HP <= 0) {
      "You died."
    } else {
        this.currentLocation.enemy = None
        "Enemy defeated!"
    }
  }

  // Returns your current health
  def health = {
    "You have " + max(0, this.HP) + " HP."
  }

  def help = {
      "The objective of the game is to find and kill all enemies in the dungeon. After that the exit will appear and you need to find it.\n" +
      "Once you have found and entered the exit, you will have beat the game.\n" +
      "At the start of the game you have 100 HP (that is also the maximum), if at any point your health goes to 0 you have lost the game.\n\n" +
      "New commands:\n" +
      "\"use\": Use this to attack the enemy in the area you're currently in or to use a heal item outside of combat.\n" +
      "\"health\": Use this to check your current health outside of combat\n" +
      "\"info\": Use this to check the stats of an item you're carrying.\n\n" +
      "See walkthrough.txt for a more comprehensive tutorial."
  }

  def inventory = {
    if (this.myItems.nonEmpty) {
      "You are carrying: \n" + this.myItems.keys.mkString("\n")
    } else {
      "No items in your inventory."
    }
  }

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }


  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


}


