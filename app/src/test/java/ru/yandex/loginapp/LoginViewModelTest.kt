package ru.yandex.loginapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `EmptyFieldError state when login with empty email and password`() {
        val email = ""
        val password = ""
        val expected = LoginScreenState.EmptyFieldsError

        viewModel.login(email, password)

        assertEquals(expected,viewModel.state.value)
    }

    @Test
    fun `EmailValidationError state when email is invalid`() {
        val invalidEmail = INVALID_EMAIL
        val password = PASSWORD
        val expected = LoginScreenState.EmailValidationError

        viewModel.login(invalidEmail, password)

        assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `Loading state when valid data`() {
        val email = VALID_EMAIL
        val password = PASSWORD
        val expected = LoginScreenState.Loading

        viewModel.login(email, password)
        testDispatcher.scheduler.runCurrent()

        assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `Success state when valid data`() {
        val email = VALID_EMAIL
        val password = PASSWORD
        val expected = LoginScreenState.Success

        viewModel.login(email,password)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, viewModel.state.value)
    }

    private companion object {
        const val VALID_EMAIL = "valid@email.com"
        const val INVALID_EMAIL = "инвалид@мэйл.ру"
        const val PASSWORD = "some_password"
    }
}