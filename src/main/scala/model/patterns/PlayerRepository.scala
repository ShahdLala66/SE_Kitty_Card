// src/main/scala/model/PlayerRepository.scala
package model

import play.api.libs.json._
import java.io.{File, PrintWriter}

case class PlayerData(name: String, points: Int, level: Int, money: Int)

object PlayerData {
  implicit val playerDataFormat: Format[PlayerData] = Json.format[PlayerData]
}

object PlayerRepository {
  private val filePath = "players.json"

  def loadPlayers(): List[PlayerData] = {
    val file = new File(filePath)
    if (file.exists()) {
      val source = scala.io.Source.fromFile(file)
      val json = try source.mkString finally source.close()
      Json.parse(json).as[List[PlayerData]]
    } else {
      List.empty[PlayerData]
    }
  }

  def savePlayers(players: List[PlayerData]): Unit = {
    val json = Json.toJson(players).toString()
    val writer = new PrintWriter(new File(filePath))
    try writer.write(json) finally writer.close()
  }

  def addPlayer(player: PlayerData): Unit = {
    val players = loadPlayers()
    savePlayers(players :+ player)
  }
}