package util

import model.{Grid, Player}

class GameState(private val grid: Grid, players: List[Player], currentPlayerIndex: Int) {

  def getCurrentPlayer: Player = {
    players(currentPlayerIndex)
  }

  def getGrid: Grid = {
    grid
  }

  def updateGrid(newGrid: Grid): GameState = {
    new GameState(newGrid, players, currentPlayerIndex)
  }

  def nextPlayer(): GameState = {
    new GameState(grid, players, (currentPlayerIndex + 1) % players.length)
  }

  def deepCopy(): GameState = {
    new GameState(grid.copy(), players, currentPlayerIndex)
  }
}