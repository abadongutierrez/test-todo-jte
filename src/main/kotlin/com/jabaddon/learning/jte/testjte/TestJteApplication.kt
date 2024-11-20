package com.jabaddon.learning.jte.testjte

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent
import de.tschuehly.spring.viewcomponent.jte.ViewContext
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class TestJteApplication

fun main(args: Array<String>) {
    runApplication<TestJteApplication>(*args)
}

data class Page(val title: String, val description: String)

@ViewComponent
class VerySimpleViewComponent {
    fun render(message: String = "Hello World") = VerySimpleView(message)

    data class VerySimpleView(val message: String) : ViewContext
}

@Controller
class TestController(private val verySimpleViewComponent: VerySimpleViewComponent) {


    @GetMapping("/")
    fun index(model: Model, response: HttpServletResponse): String {
        model.addAttribute("page", Page("This is the title", "This is the description"))
        return "index"
    }

    @GetMapping("/hello")
    fun hello(model: Model, response: HttpServletResponse): String {
        model.addAttribute("page", Page("Hello", "Hello World"))
        return "hello"
    }

    @GetMapping("/vc/hello")
    fun viewComponentRender(model: Model, response: HttpServletResponse): ViewContext {
        return verySimpleViewComponent.render("Hello World with ViewComponents")
    }
}
