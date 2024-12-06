// src/main/scala/model/patterns/PlayerRepository.scala
package model.patterns

import play.api.libs.json._
import java.io.{File, PrintWriter}

case class PlayerData(name: String, points: Int, level: Int, money: Int)

object PlayerData {
  implicit val playerDataFormat: Format[PlayerData] = Json.format[PlayerData]
}

trait PlayerRepository {
  def loadPlayers(): List[PlayerData]
  def addPlayer(player: PlayerData): Unit
}

object PlayerRepositoryImpl extends PlayerRepository {
  private val filePath = "players.json"

  override def loadPlayers(): List[PlayerData] = {
    val file = new File(filePath)
    if (file.exists()) {
      val source = scala.io.Source.fromFile(file)
      val json = try source.mkString finally source.close()
      Json.parse(json).as[List[PlayerData]]
    } else {
      List.empty[PlayerData]
    }
  }

  override def addPlayer(player: PlayerData): Unit = {
    val players = loadPlayers()
    savePlayers(players :+ player)
  }

  private def savePlayers(players: List[PlayerData]): Unit = {
    val json = Json.toJson(players).toString()
    val writer = new PrintWriter(new File(filePath))
    try writer.write(json) finally writer.close()
  }
}