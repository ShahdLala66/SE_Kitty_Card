package util

import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.net.URI
import java.nio.charset.StandardCharsets
import scala.util.{Try, Success, Failure}

object GameStateHttpClient {
  private val client = HttpClient.newHttpClient()
  private val serverUrl = "http://localhost:9000"
  
  def sendUpdate(update: String): Unit = {
    Try {
      val request = HttpRequest.newBuilder()
        .uri(URI.create(s"$serverUrl/api/update"))
        .header("Content-Type", "text/plain")
        .POST(HttpRequest.BodyPublishers.ofString(update, StandardCharsets.UTF_8))
        .build()
      
      val response = client.send(request, HttpResponse.BodyHandlers.ofString())
      
      if (response.statusCode() != 200) {
        println(s"Warning: Failed to send update to server. Status: ${response.statusCode()}")
      }
    } match {
      case Success(_) => // Successfully sent
      case Failure(e) => 
        // Silently fail if server is not available
        // println(s"Could not connect to game server: ${e.getMessage}")
    }
  }
}
