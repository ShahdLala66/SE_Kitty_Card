import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import com.google.inject.{Guice, Injector}
import controller.*
import controller.baseImp.GameController
import model.fileIOComp.*
import model.fileIOComp.baseImp.{FileIOJSON, FileIOXML}

class MainModuleSpec extends AnyWordSpec with Matchers {

    "JsonModule" should {
        "provide the correct FileIO implementation" in {
            val injector: Injector = Guice.createInjector(new JsonModule)
            val fileIO = injector.getInstance(classOf[FileIOInterface])
            fileIO shouldBe a[FileIOJSON]
        }

        "provide a GameController with correct dependencies" in {
            val injector: Injector = Guice.createInjector(new JsonModule)
            val gameController = injector.getInstance(classOf[GameControllerInterface])
            gameController shouldBe a[GameController]
        }
    }

    "XmlModule" should {
        "provide the correct FileIO implementation" in {
            val injector: Injector = Guice.createInjector(new XmlModule)
            val fileIO = injector.getInstance(classOf[FileIOInterface])
            fileIO shouldBe a[FileIOXML]
        }

        "provide a GameController with correct dependencies" in {
            val injector: Injector = Guice.createInjector(new XmlModule)
            val gameController = injector.getInstance(classOf[GameControllerInterface])
            gameController shouldBe a[GameController]
        }
    }
}