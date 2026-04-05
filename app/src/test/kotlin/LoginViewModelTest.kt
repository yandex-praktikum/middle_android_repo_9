import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.yandex.loginapp.LoginScreenState
import ru.yandex.loginapp.LoginViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModelTest: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        viewModelTest = LoginViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty fields error test`() {
        viewModelTest.login("", "")

        assertEquals(viewModelTest.state.value, LoginScreenState.EmptyFieldsError)
    }

    @Test
    fun `wrong email test`() {
        viewModelTest.login("somestring", "123456")

        assertEquals(viewModelTest.state.value, LoginScreenState.EmailValidationError)
    }

    @Test
    fun `loading test`() {
        viewModelTest.login("somestring@yandex.ru", "123456")

        testDispatcher.scheduler.runCurrent()
        assertEquals(viewModelTest.state.value, LoginScreenState.Loading)
    }

    @Test
    fun `success test`() {
        viewModelTest.login("somestring@yandex.ru", "123456")

        testDispatcher.scheduler.runCurrent()
        testDispatcher.scheduler.advanceTimeBy(3001)
        assertEquals(viewModelTest.state.value, LoginScreenState.Success)
    }
}