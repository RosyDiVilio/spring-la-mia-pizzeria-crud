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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	//CREATE
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pizza", new Pizza());
		
		return "pizzeria/create";
	}
	
	//STORE
	
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, 
			            BindingResult bindingResult,
			            Model model,
			            RedirectAttributes attributes) {
		
		if(bindingResult.hasErrors()) {
			return "/pizzeria/create";
		} else {
			repo.save(formPizza);
			attributes.addFlashAttribute("successMessage", formPizza.getName() + " has been created successfully");
			return "redirect:/pizzas";
		}
		
	}
	
	//EDIT => una GET che restituisce al chiamante un form con i dati della risorsa richiesta
	
	 @GetMapping("/edit/{id}")
	 public String edit(@PathVariable("id") Integer id, Model model) {
		 
		 //trovo la pizza
		// Pizza pizzaToEdit = repo.findById(id).get();
		 
		 //la insierisco nel model
		// model.addAttribute("pizza", pizzaToEdit); 
		 
		 model.addAttribute("pizza", repo.findById(id).get());
		 
		 //restituisco la view con il model inserito
		 return "/pizzeria/edit";
	 }
	
	//UPDATE => una POST che aggiorna nel DB i nuovi dati inseriti, se corretti, della risorsa
	
	 @PostMapping("/edit/{id}")
	 public String update(@Valid @ModelAttribute("pizza") Pizza updatedFormPizza, 
				          BindingResult bindingResult,
				          Model model,
				          RedirectAttributes attributes) {
		 
		 //se ci sono errori nel from
		 if(bindingResult.hasErrors()) {
		    //ritorna nel form e mostra gli errori	 
			return "/pizzas/edit";
		 } else {
		      //altriemnti prendi la repo e aggiorna la pizza con i nuovi dati	 
			 repo.save(updatedFormPizza);
		     //ridireziona l'utente alla index delle pizze
			 attributes.addFlashAttribute("successMessage", updatedFormPizza.getName() + " has been updated successfully");
			 return "redirect:/pizzas";
		 }
	 }
	 
	 //DELETE
	 
	 @PostMapping("/delete/{id}") 
		 public String delete(@PathVariable("id") Integer id,
				              RedirectAttributes attributes) {
		 
		 //prendi la repo e dopo aver trovato la pizza tramite l'id, cancellalo dal DB
		 repo.deleteById(id);
			 
		 //ridireziona l'utente alla index delle pizze
		     attributes.addFlashAttribute("successMessage", "Pizza with id " + id + " has been deleted successfully");
			 return "redirect:/pizzas";
		 }
		 
		
		 
}
	
