import java.nio.ByteBuffer
import java.nio.channels.{Channels, ReadableByteChannel}
import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import javax.swing._

object GUIInterruptExample {
  @volatile var guiInput: Option[String] = None
  val inputReceived = new AtomicBoolean(false) // Flag to track if input is received from GUI

  def main(args: Array[String]): Unit = {
    // Set up GUI
    val frame = new JFrame("Input Example")
    val textField = new JTextField(20)
    val button = new JButton("Submit")
    val panel = new JPanel()

    panel.add(textField)
    panel.add(button)
    frame.add(panel)
    frame.setSize(300, 100)
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE) // Correct usage
    frame.setVisible(true)

    // Handle GUI input
    button.addActionListener(_ => {
      val input = textField.getText.trim
      if (input.nonEmpty) {
        guiInput = Some(input)
        inputReceived.set(true) // Signal that input is received
        println(s"GUI Input Received: $input")
      }
    })

    // Handle console input in a separate thread
    val consoleInputFuture = readConsoleInput()

    // Monitor for GUI input to interrupt console input
    Future {
      while (!inputReceived.get()) {
        Thread.sleep(100) // Check every 100ms
      }
      println("Interrupting console input due to GUI input.")
      consoleInputFuture.foreach(_.interrupt()) // Interrupt the console input thread
    }
  }

  def readConsoleInput(): Future[Thread] = Future {
    val thread = Thread.currentThread()
    val channel: ReadableByteChannel = Channels.newChannel(System.in)
    val buffer: ByteBuffer = ByteBuffer.allocate(256)

    try {
      println("Type in the console (or input in GUI to interrupt):")
      while (!inputReceived.get()) {
        buffer.clear() // Prepare buffer for reading
        val bytesRead = channel.read(buffer)
        if (bytesRead == -1) {
          println("End of console input.")
        }
        buffer.flip() // Prepare buffer for writing
        val input = new String(buffer.array(), buffer.position(), buffer.remaining()).trim
        println(s"Console Input: $input")
      }
    } catch {
      case _: InterruptedException =>
        println("Console input was interrupted.")
    } finally {
      channel.close()
    }

    thread // Returning the thread implicitly as the last expression
  }

}
