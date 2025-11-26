package controller

import model.gameModelComp.baseImp.{GameSession, Player}
import scala.collection.mutable

class SessionManager {
  private val sessions = mutable.Map[String, GameSession]()
  private val playerSessions = mutable.Map[String, String]() // playerId -> sessionId
  
  def createSession(): GameSession = {
    val sessionId = GameSession.generateSessionId()
    val session = GameSession(sessionId)
    sessions.put(sessionId, session)
    session
  }
  
  def getSession(sessionId: String): Option[GameSession] = {
    sessions.get(sessionId)
  }
  
  def joinSession(sessionId: String, playerName: String, playerId: String): Option[Int] = {
    sessions.get(sessionId).flatMap { session =>
      val playerNumber = addPlayerToSession(session, playerName, playerId)
      playerNumber.foreach { _ =>
        playerSessions.put(playerId, sessionId)
      }
      playerNumber
    }
  }
  
  def getPlayerSession(playerId: String): Option[GameSession] = {
    playerSessions.get(playerId).flatMap(sessions.get)
  }
  
  def removeSession(sessionId: String): Unit = {
    sessions.remove(sessionId)
    playerSessions.filterInPlace((_, sid) => sid != sessionId)
  }
  
  def isPlayerInSession(playerId: String, sessionId: String): Boolean = {
    playerSessions.get(playerId).contains(sessionId)
  }
  
  // Logic methods moved from GameSession
  
  def addPlayerToSession(session: GameSession, playerName: String, playerId: String): Option[Int] = {
    if (session.player1.isEmpty) {
      session.player1 = Some(Player(playerName))
      session.player1Id = Some(playerId)
      Some(1)
    } else if (session.player2.isEmpty) {
      session.player2 = Some(Player(playerName))
      session.player2Id = Some(playerId)
      Some(2)
    } else {
      None
    }
  }
    def getPlayerNumber(session: GameSession, playerId: String): Option[Int] = {
    if (session.player1Id.contains(playerId)) Some(1)
    else if (session.player2Id.contains(playerId)) Some(2)
    else None
  }
  
  def isPlayerTurn(session: GameSession, playerId: String): Boolean = {
    val result = session.currentPlayerId.contains(playerId)
    println(s"[SessionManager] isPlayerTurn check: playerId=$playerId, currentPlayerId=${session.currentPlayerId}, result=$result")
    result
  }
  
  def switchTurn(session: GameSession): Unit = {
    if (session.player1Id.isDefined && session.player2Id.isDefined) {
      val oldPlayerId = session.currentPlayerId
      session.currentPlayerId = if (session.currentPlayerId == session.player1Id) {
        session.player2Id
      } else {
        session.player1Id
      }
      println(s"[SessionManager] Switched turn from $oldPlayerId to ${session.currentPlayerId}")
    }
  }
  
  def startGameSession(session: GameSession): Unit = {
    if (session.isReady && !session.isStarted) {
      session.isStarted = true
      session.currentPlayerId = session.player1Id
      println(s"[SessionManager] Game started! sessionId=${session.sessionId}, player1Id=${session.player1Id}, player2Id=${session.player2Id}, currentPlayerId=${session.currentPlayerId}")
    }
  }
}
