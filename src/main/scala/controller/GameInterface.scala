package controller

import model.playerComp.Player
import util.GameEvent
import util.command.GameState

trait GameInterface {
    def askForPlayerNames(): Unit
    def start(player1Name: String, player2Name: String): Unit
    def distributeInitialCards(): Unit
    def gameLoop(): Unit
    def handlePlayerTurn(): Unit
    def drawCardForCurrentPlayer(): Unit
    def processPlayerInput(): Unit
    def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean
    def handleCardPlacement(input: String): Boolean
    def getCurrentplayer: Player
    def switchTurns(): Unit
    def displayFinalScores(): Unit
    def addPlayers(player1Name: String, player2Name: String): (Player, Player)
}