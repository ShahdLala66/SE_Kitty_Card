package util

import java.io.{BufferedReader, InputStreamReader}
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class ConsoleProvider extends InputProvider {
    private val reader = new BufferedReader(new InputStreamReader(System.in))
    private val shouldInterrupt = new AtomicBoolean(false)
    private var currentThread: Option[Thread] = None

    override def getInput: String = {
        shouldInterrupt.set(false)
        var input: String = null

        // Store the current thread so we can interrupt it
        currentThread = Some(Thread.currentThread())

        try {
            // Check if input is available before blocking
            while (!shouldInterrupt.get() && !reader.ready()) {
                TimeUnit.MILLISECONDS.sleep(100)  // Small delay to prevent CPU spinning
            }

            if (shouldInterrupt.get()) {
                return null  // Return null if interrupted
            }

            input = reader.readLine()
        } catch {
            case _: InterruptedException =>
                return null  // Return null if interrupted
            case e: Exception =>
                e.printStackTrace()
                return null
        } finally {
            currentThread = None
        }

        input
    }

    override def interrupt(): Unit = {
        shouldInterrupt.set(true)
        currentThread.foreach(_.interrupt())
    }
}