// src/main/scala/util/GameState.scala
package util.command

import model.gameModelComp.baseImp.{Grid, Player}

class GameState(val grid: Grid, val players: List[Player], val currentPlayerIndex: Int, val points: Int) {

    def getGrid: Grid = grid

    def getPlayers: List[Player] = players

    def getCurrentPlayer: Player = players(currentPlayerIndex)


    def getPoints: Int = points

    def updateGrid(newGrid: Grid): GameState = {
        new GameState(newGrid, players, currentPlayerIndex, points)
    }

    def nextPlayer(): GameState = {
        new GameState(grid, players, (currentPlayerIndex + 1) % players.length, points)
    }

    def update(newGrid: Grid, newPoints: Int): GameState = {
        new GameState(newGrid, players, currentPlayerIndex, newPoints)
    }

    def getCurrentPlayerIndex: Int = currentPlayerIndex

}
