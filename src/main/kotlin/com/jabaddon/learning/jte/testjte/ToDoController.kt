package com.jabaddon.learning.jte.testjte

import com.jabaddon.learning.jte.testjte.EmptyItem.ItemView
import de.tschuehly.spring.viewcomponent.core.component.ViewComponent
import de.tschuehly.spring.viewcomponent.jte.ViewContext
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

data class ToDoItem(val id: Long, var title: String, var completed: Boolean = false)

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

  fun addToDo(todo: String) {
    todos.add(ToDoItem(todos.size + 1L, todo))
  }

  fun completeToDo(id: Long, value: Boolean) {
    findById(id)?.let {
      it.completed = value
    }
  }

  fun findById(id: Long): ToDoItem? {
    return todos.find { it.id == id }
  }

  fun editToDo(id: Long, todo: String) {
    findById(id)?.let {
      it.title = todo
    }
  }

  fun deleteToDo(id: Long) {
    todos.removeIf { it.id == id }
  }
}

@Controller
class ApiToDoController(
  val toDoService: ToDoService,
  val toDoList: TODOList,
  val item: Item,
  val emptyItem: EmptyItem,
  val toDoHero: TODOHero
) {
  @PostMapping("/api/todos")
  fun newTodo(@RequestParam todo: String, response: HttpServletResponse) {
    toDoService.addToDo(todo)
    response.addHeader("HX-Trigger", "todo-added, todo-completed")
  }

  @DeleteMapping("/api/todos/{id}")
  fun delete(@PathVariable id: Long, response: HttpServletResponse) {
    toDoService.deleteToDo(id)
    response.addHeader("HX-Trigger", "todo-added, todo-completed")
  }

  @GetMapping("/fragments/todo")
  fun todoList(): ViewContext {
    return toDoList.render(toDoService.getToDos())
  }

  @GetMapping("/fragments/todohero")
  fun todoHero(): ViewContext {
    return toDoHero.render(
      completed = toDoService.getToDos().count { it.completed },
      total = toDoService.getToDos().size
    )
  }

  @PostMapping("/fragments/todo/{id}/complete")
  fun completeTodo(@PathVariable id: Long, response: HttpServletResponse): ViewContext {
    toDoService.completeToDo(id, true)
    response.addHeader("HX-Trigger", "todo-completed")
    return toDoService.findById(id)?.let {
      item.render(it)
    } ?: emptyItem.render()
  }

  @PostMapping("/fragments/todo/{id}/uncomplete")
  fun uncompleteTodo(@PathVariable id: Long, response: HttpServletResponse): ViewContext {
    toDoService.completeToDo(id, false)
    response.addHeader("HX-Trigger", "todo-completed")
    return toDoService.findById(id)?.let {
      item.render(it)
    } ?: emptyItem.render()
  }

  @PostMapping("/fragments/todo/{id}/edit")
  fun edit(@PathVariable id: Long): ViewContext {
    return toDoService.findById(id)?.let {
      item.render(it, editing = true)
    } ?: emptyItem.render()
  }

  @PutMapping("/fragments/todo/{id}")
  fun saveEdit(@PathVariable id: Long, @RequestParam todo: String): ViewContext {
    toDoService.editToDo(id, todo)
    return toDoService.findById(id)?.let {
      item.render(it, editing = false)
    } ?: emptyItem.render()
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
  ) : ViewContext
}

@ViewComponent
class Header {
  fun render() = HeaderView()
  class HeaderView() : ViewContext
}

@ViewComponent
class TODOHero {
  fun render(completed: Int, total: Int) = TODOHeroView(
    completed = completed,
    total = total
  )

  data class TODOHeroView(val completed: Int, val total: Int) : ViewContext
}

@ViewComponent
class Form() {
  fun render() = FormView()
  class FormView() : ViewContext
}

@ViewComponent
class TODOList(val item: Item) {
  fun render(todos: List<ToDoItem>) = TODOListView(
    items = todos,
    item = item
  )

  class TODOListView(val items: List<ToDoItem>, val item: Item) : ViewContext
}

@ViewComponent
class Item {
  fun render(item: ToDoItem, editing: Boolean = false) = ItemView(item, editing)
  class ItemView(val item: ToDoItem, val editing: Boolean) : ViewContext
}

@ViewComponent
class EmptyItem {
  fun render() = ItemView()
  class ItemView() : ViewContext
}