package org.lessons.java.pizzeria.controller;

import java.util.List;

import org.lessons.java.pizzeria.model.Pizza;
import org.lessons.java.pizzeria.repo.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

//controller = prende dati dal model e li rendere disponibili alla view
@Controller
@RequestMapping("/pizzas")
public class PizzaController {
	
	//repository field con autowired per D.I.
	@Autowired
	private PizzaRepository repo;
	
	@GetMapping
	public String index(Model model) {
		
		//creo lista pizza e dopo prendo i dati dal DB
		List<Pizza> pizzaList = repo.findAll();
		
		//inserisco dati nel modello
		model.addAttribute("pizzas", pizzaList);
		return "pizzeria/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") Integer id, Model model) {
		Pizza pizza = repo.findById(id).get();
		model.addAttribute("pizza", pizza);
		
		return "pizzeria/show";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pizza", new Pizza());
		
		return "pizzeria/create";
	}
	
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, 
			BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/pizzas/create";
		} else {
			repo.save(formPizza);
			return "redirect:/pizzas";
		}
		
	}
	
}
