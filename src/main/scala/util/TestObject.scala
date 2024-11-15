// src/main/scala/util/TestObject.scala
package util

class TestObject extends Observer {
  def update(event: GameEvent): Unit = println("Ping")
}