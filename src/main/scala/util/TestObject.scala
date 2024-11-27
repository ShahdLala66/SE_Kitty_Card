// src/main/scala/util/TestObject.scala
package util

class TestObject extends Observer {
    private var lastNotification: Option[GameEvent] = None

    override def update(event: GameEvent): Unit = lastNotification = Some(event)
}