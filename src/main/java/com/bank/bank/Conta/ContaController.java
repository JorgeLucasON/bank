package com.bank.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contas")
public class ContaController {
    @Autowired
    private ContaService contaService;

    @GetMapping
    public String listarContas (Model model){
        List<Conta>contas=contaService.listarTodas();
        model.addAttribute("contas",contas);
        model.addAttribute("conta", new Conta());
        return "contas";
    }

    @PostMapping("/adicionar")
    public String adicionarConta(@ModelAttribute Conta conta){
        contaService.salvar(conta);
        return "redirect:/contas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormEdicao(@PathVariable Long id, Model model){
        Conta conta = contaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta inválida: " + id));
        model.addAttribute("conta",conta);
        return "editar-conta";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarConta(@PathVariable Long id,@ModelAttribute Conta conta){
        contaService.atualizar(id,conta);
        return "redirect:/contas";
    }

    @GetMapping("/deletar/{id}")
    public String deletarConta(@PathVariable Long id){
        contaService.deletar(id);
        return "redirect:/contas";
    }
}
