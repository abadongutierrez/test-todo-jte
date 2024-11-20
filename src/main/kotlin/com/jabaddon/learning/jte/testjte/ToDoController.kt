package com.jabaddon.learning.jte.testjte

import de.tschuehly.spring.viewcomponent.core.component.ViewComponent
import de.tschuehly.spring.viewcomponent.jte.ViewContext
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

data class ToDoItem(val id: Long, val title: String, val completed: Boolean = false)

@Service
class ToDoService {
    private val todos = mutableListOf(
        ToDoItem(1, "Buy milk"),
        ToDoItem(2, "Walk the dog"),
        ToDoItem(3, "Pay bills")
    )

    fun getToDos(): List<ToDoItem> {
        return todos
    }
}

@Controller
@RequestMapping("/todo")
class ToDoController(
    private val todoHome: TODOHome
) {
    @GetMapping("/test")
    fun test(): String {
        return "test"
    }

    @GetMapping
    fun index(): ViewContext {
        return todoHome.render()
    }
}

@ViewComponent
class TODOHome(
    val toDoService: ToDoService,
    val header: Header,
    val toDoHero: TODOHero,
    val form: Form,
    val toDoList: TODOList
) {
    fun render() = ToDoView(
        header = header.render(),
        toDoHero = toDoHero.render(
            completed = toDoService.getToDos().count { it.completed },
            total = toDoService.getToDos().size
        ),
        form = form.render(),
        toDoList = toDoList.render(todos = toDoService.getToDos())
    )
    data class ToDoView(
        val header: ViewContext,
        val toDoHero: ViewContext,
        val form: ViewContext,
        val toDoList: ViewContext
    ): ViewContext
}

@ViewComponent
class Header {
    fun render() = HeaderView()
    class HeaderView(): ViewContext
}

@ViewComponent
class TODOHero {
    fun render(completed: Int, total: Int) = TODOHeroView(
        completed = completed,
        total = total
    )
    data class TODOHeroView(val completed: Int, val total: Int): ViewContext
}

@ViewComponent
class Form() {
    fun render() = FormView()
    class FormView(): ViewContext
}

@ViewComponent
class TODOList(val item: Item) {
    fun render(todos: List<ToDoItem>) = TODOListView(
        items = todos,
        item = item
    )
    class TODOListView(val items: List<ToDoItem>, val item: Item): ViewContext
}

@ViewComponent
class Item {
    fun render(item: ToDoItem) = ItemView(item)
    class ItemView(val item: ToDoItem): ViewContext
}