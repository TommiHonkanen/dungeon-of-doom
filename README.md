# Dungeon of Doom

Dungeon of Doom is an adventure game playable in the console. The objective of the game is to get out of the dungeon alive. In order to accomplish the player has to eliminate all enemies in the dungeon and after that find the exit.

At the start of the game, 3 weapons, 4 heal items and 3 enemies are scattered randomly across the map. The player doesn't necessarily have to find all heal items and weapons but they can help beat the game.

At the start of the game the player has 100 HP which is also the maximum. If the player takes too much damage and the HP goes to 0, the player dies and loses the game. The player can use the heal items using the "use" command. Current health can be checked at any time outside of combat using the "health" command.

There can never be more than 1 enemy in a single area. In order to attack the enemy in the player's current location, one has to use a weapon currently in their inventory using the "use" command e.g. "use hammer". That will provoke the enemy and initiate the combat mechanism.

Commands:
    "go": Use this to navigate the dungeon e.g. "go south".
    "quit": Ends the game (can't be used during combat).
    "get": Use this to pick up an item in your current location e.g. "get hammer".
    "info": Use this to check the stats of an item you're carrying e.g. "info hammer".
    "inventory": Use this to check what items you're carrying.
    "drop": Use this to drop an item you're carrying into your current location e.g. "drop hammer".
    "use": - Use this to initiate combat with the enemy in your current location e.g. "use hammer".
           - Use this to use a heal item outside of combat e.g. "use apple".
    "health": Use this to check your current health outside of combat.
    "help": Some basic info about the game.
