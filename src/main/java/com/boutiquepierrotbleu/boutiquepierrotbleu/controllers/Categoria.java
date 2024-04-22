package com.boutiquepierrotbleu.boutiquepierrotbleu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.ValorCategoria;
import com.boutiquepierrotbleu.boutiquepierrotbleu.services.CategoriaService;

@Controller
@RequestMapping("categoria")
public class Categoria {
    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping("novo")
    public ModelAndView salvarCategoria(@RequestParam(required = false) Long id) {
        
        ModelAndView mv = new ModelAndView("categoria/cadastro");
        ValorCategoria categoria = new ValorCategoria();
        if(id != null) {
            try {
                categoria = categoriaService.obterCategoria(id);
                mv.addObject("categoria", categoria);
                
                return mv;
            } catch (Exception e) {
                mv.addObject("categoria", e.getMessage());
            }
        }
        mv.addObject("categoria", categoria);
        return mv;
    }

    @RequestMapping("editar")
    public ModelAndView editarCategoria(@RequestParam(required = false) Long id) {
        
        ModelAndView mv = new ModelAndView("categoria/cadastro");
        ValorCategoria categoria = new ValorCategoria();
        if(id != null) {
            try {
                categoria = categoriaService.obterCategoria(id);
                mv.addObject("categoria", categoria);
                
                return mv;
            } catch (Exception e) {
                mv.addObject("categoria", e.getMessage());
            }
        }
        mv.addObject("categoria", categoria);
        return mv;
    }

    @PostMapping("salvar")
    public ModelAndView categoriaSalva(@ModelAttribute ValorCategoria categoria, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("categoria/cadastro");
            mv.addObject("categoria", categoria);
            return mv;
        }
        ModelAndView mv = new ModelAndView("categoria/lista");
        
        categoriaService.salvarCategoria(categoria);

        redirectAttributes.addFlashAttribute("mensagem", "Categoria salva com sucesso!");
        return mv;
    }


    @RequestMapping("listar")
    public ModelAndView listarCategorias() {
        ModelAndView mv = new ModelAndView("categoria/listar");
        List<ValorCategoria> categorias = categoriaService.getCategorias();
        mv.addObject("categorias", categorias);
        return mv;
    }
}
