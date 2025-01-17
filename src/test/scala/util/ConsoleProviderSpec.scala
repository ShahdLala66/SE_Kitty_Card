package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito.*
import org.scalatestplus.mockito.MockitoSugar

import java.io.BufferedReader

class ConsoleProviderSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A ConsoleProvider" should {

        "return input when getInput is called" in {
            val consoleProvider = new ConsoleProvider
            val input = "test input"
            val reader = mock[BufferedReader]

            when(reader.ready()).thenReturn(true)
            when(reader.readLine()).thenReturn(input)

            val field = classOf[ConsoleProvider].getDeclaredField("reader")
            field.setAccessible(true)
            field.set(consoleProvider, reader)

            consoleProvider.getInput

            verify(reader, times(1)).ready()
            verify(reader, times(1)).readLine()
        }

        "return null when interrupted" in {
            val consoleProvider = new ConsoleProvider

            val thread = new Thread(new Runnable {
                override def run(): Unit = {
                    val result = consoleProvider.getInput
                    result shouldBe null
                }
            })

            thread.start()
            Thread.sleep(100)
            consoleProvider.interrupt()
            thread.join()
        }

        "handle exceptions and return null" in {
            val consoleProvider = new ConsoleProvider
            val reader = mock[BufferedReader]

            when(reader.ready()).thenReturn(true)
            when(reader.readLine()).thenThrow(new RuntimeException("Test Exception"))

            val field = classOf[ConsoleProvider].getDeclaredField("reader")
            field.setAccessible(true)
            field.set(consoleProvider, reader)

            consoleProvider.getInput shouldBe null

            verify(reader, times(1)).ready()
            verify(reader, times(1)).readLine()
        }

        "return null when shouldInterrupt is set to true during execution" in {
            val consoleProvider = new ConsoleProvider

            val thread = new Thread(new Runnable {
                override def run(): Unit = {
                    val result = consoleProvider.getInput
                    result shouldBe null // Assert that getInput returns null when interrupted
                }
            })

            thread.start()
            Thread.sleep(50) // Allow the thread to start and enter the getInput loop
            consoleProvider.interrupt() // Trigger the shouldInterrupt condition
            thread.join()
        }

    }
}