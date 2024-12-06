// src/main/scala/model/patterns/SinglePlayerMode.scala
package model.patterns

import model.{Game, Player}

class SinglePlayerMode(game: Game) extends GameMode {
  override def startGame(): Unit = {
    val player = selectOrCreatePlayer()
  }

  override def playTurn(): Unit = {
    println("Single-player mode is coming soon!")
  }

  override def endGame(): Unit = {
    println("Ending single player game...")
  }

  override def isGameOver: Boolean = {
    false
  }

  private def selectOrCreatePlayer(): Player = {
    println("Do you want to (1) select an existing player or (2) create a new player?")
    scala.io.StdIn.readLine().trim match {
      case "1" => selectExistingPlayer()
      case "2" => createNewPlayer()
      case _ =>
        println("Invalid choice, creating a new player by default.")
        createNewPlayer()
    }
  }

  private def selectExistingPlayer(): Player = {
    val players = PlayerRepository.loadPlayers()
    if (players.isEmpty) {
      println("No existing players found. Creating a new player.")
      createNewPlayer()
    } else {
      println("Select a player by number:")
      players.zipWithIndex.foreach { case (player, index) =>
        println(s"${index + 1}. ${player.name} (Points: ${player.points}, Level: ${player.level}, Money: ${player.money})")
      }
      val choice = scala.io.StdIn.readLine().trim.toIntOption.getOrElse(0)
      if (choice > 0 && choice <= players.length) {
        val selectedPlayer = players(choice - 1)
        Player(selectedPlayer.name, selectedPlayer.points)
      } else {
        println("Invalid choice, creating a new player by default.")
        createNewPlayer()
      }
    }
  }

  private def createNewPlayer(): Player = {
    println("Enter the name for the new player:")
    val name = scala.io.StdIn.readLine().trim
    val newPlayer = PlayerData(name, 0, 1, 0)
    PlayerRepository.addPlayer(newPlayer)
    Player(newPlayer.name, newPlayer.points)
  }
}