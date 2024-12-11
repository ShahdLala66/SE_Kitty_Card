import logik.GameModeL
import model.logik.{AdvancedGameMode, NormalGameMode}
import model.objects.{Deck, Grid, Player}

import java.util.Observable

class Game(deck: Deck, grid: Grid, var gameMode: GameModeL) extends Observable {
  private var currentPlayer: Player = _
  private var player1: Player = _
  private var player2: Player = _

  /**
   * Starts the game based on the provided mode and player names.
   * The mode (Normal or Advanced) is determined by the `isAdvanced` flag.
   */
  def start(player1Name: String, player2Name: String, isAdvanced: Boolean): Unit = {
    player1 = new Player(player1Name)
    player2 = new Player(player2Name)
    currentPlayer = player1

    // Using GameModeFactory to determine the game mode
    gameMode = if (isAdvanced)
      new AdvancedGameMode(grid, deck)
    else
      new NormalGameMode(grid, deck)

    // Distribute initial cards to players
    distributeInitialCards()

    // Notify observers of game start
    notifyObserversOnChange()

    // Start main game loop
    gameMode.startGameLoop(currentPlayer, opponentPlayer)
  }

  /**
   * Distributes initial cards to both players.
   */
  private def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
  }

  /**
   * Gets the non-current player (opponent).
   */
  private def opponentPlayer: Player = if (currentPlayer == player1) player2 else player1

  /**
   * Notifies all observers about a state change.
   */
  private def notifyObserversOnChange(): Unit = {
    setChanged()
    notifyObservers(this) // Pass the current game state
  }
}