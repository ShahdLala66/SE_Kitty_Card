package model.patterns

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{File, PrintWriter}
import play.api.libs.json.Json

class PlayerRepositorySpec extends AnyWordSpec with Matchers {

    class TestPlayerRepository(testFilePath: String) extends PlayerRepository {
        override def loadPlayers(): List[PlayerData] = {
            val file = new File(testFilePath)
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
            val writer = new PrintWriter(new File(testFilePath))
            try writer.write(json) finally writer.close()
        }
    }

    "PlayerRepository" should {

        "load players from a file" in {
            // Setup: Create a temporary file with player data
            val tempFile = File.createTempFile("players", ".json")
            tempFile.deleteOnExit()
            val writer = new PrintWriter(tempFile)
            writer.write("""[{"name":"Player1","points":100,"level":1,"money":50}]""")
            writer.close()

            // Use the test implementation
            val repository = new TestPlayerRepository(tempFile.getAbsolutePath)

            // Test: Load players
            val players = repository.loadPlayers()
            players should have size 1
            players.head.name should be("Player1")
            players.head.points should be(100)
        }

        "add a player to the file" in {
            // Setup: Create a temporary file and initialize it with an empty JSON array
            val tempFile = File.createTempFile("players", ".json")
            tempFile.deleteOnExit()
            val writer = new PrintWriter(tempFile)
            writer.write("[]")
            writer.close()

            // Use the test implementation
            val repository = new TestPlayerRepository(tempFile.getAbsolutePath)

            // Test: Add a player
            val newPlayer = PlayerData("Player2", 200, 2, 100)
            repository.addPlayer(newPlayer)

            // Verify: Load players and check the new player is added
            val players = repository.loadPlayers()
            players should have size 1
            players.head.name should be("Player2")
            players.head.points should be(200)
        }
    }
}