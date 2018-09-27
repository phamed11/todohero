package com.greenfoxacademy.connectionmysql.Controllers;

import com.greenfoxacademy.connectionmysql.Models.ToDo;
import com.greenfoxacademy.connectionmysql.Respositories.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class ToDoControllers {

  private ToDoRepository toDoRepository;

  @Autowired
  public ToDoControllers(ToDoRepository toDoRepository) {
    this.toDoRepository = toDoRepository;
  }

  @GetMapping(value = {"/", "/list"})
  public String list(Model model, @RequestParam(value = "isActive", required = false) Boolean result) {
    if (result == null) {
      model.addAttribute("todos", toDoRepository.findAll());
    } else {
      model.addAttribute("todos", toDoRepository.findByDone(!result));
    }
    return "todolist";
  }

  @GetMapping("/add")
  public String todo() {
    return "addtodo";
  }

  @PostMapping("/add")
  public String todoPost(@RequestParam(value = "name") String title,
                         @ModelAttribute(value = "urgent") boolean urgent) {
    toDoRepository.save(new ToDo(title, urgent, false));
    return "redirect:/todo/";
  }


  @GetMapping("/{id}/delete")
  public String delete(@PathVariable(value = "id") Long idToDelete) {
    toDoRepository.deleteById(idToDelete);
    return "redirect:/todo/";
  }

  @GetMapping("/{id}/edit")
  public String getEdit(@ModelAttribute(value = "id") Long id, Model model) {
    model.addAttribute("toDo", toDoRepository.findById(id).get());
    return "edit";
  }

  @PostMapping("/{id}/edit")
  public String edit(@ModelAttribute(value = "toDo") ToDo todo) {
    toDoRepository.save(todo);
    return "redirect:/todo/";
  }


  @GetMapping("/search")
  public String searchBar(@ModelAttribute(value = "search") String search, Model model) {
    List<ToDo> searchResult = new ArrayList<>();
    for (int i = 0; i < toDoRepository.count(); i++) {
      if (toDoRepository.findAll().get(i).getTitle().toLowerCase().contains(search.toLowerCase())) {
        searchResult.add(toDoRepository.findAll().get(i));
      }
    }
    model.addAttribute("todos", searchResult);
    return "todolist";
  }


}
