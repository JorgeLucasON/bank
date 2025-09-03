package com.bank.bank.Conta;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import java.util.List;


@Controller
@RequestMapping("/contas")
public class ContaController {
    @Autowired
    private ContaService contaService;

    @GetMapping
    public String listarContas(Model model) {
        List<Conta> contas = contaService.listarTodas();
        model.addAttribute("contas", contas);

        // Se não existir conta no model, cria uma nova
        if (!model.containsAttribute("conta")) {
            model.addAttribute("conta", new Conta());
        }

        return "contas";
    }

    @PostMapping("/adicionar")
    public String adicionarConta(@Valid @ModelAttribute Conta conta,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            // Se houver erro, mantém os dados no form e mostra mensagens
            model.addAttribute("contas", contaService.listarTodas());
            return "contas";
        }

        try {
            contaService.salvar(conta);
            return "redirect:/contas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contas", contaService.listarTodas());
            return "contas";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormEdicao(@PathVariable Long id, Model model){
        Conta conta = contaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta inválida: " + id));
        model.addAttribute("conta",conta);
        return "editar-conta";
    }
    @PostMapping("/atualizar/{id}")
    public String atualizarConta(@PathVariable Long id, @ModelAttribute Conta conta, Model model) {
        try {
            // Buscar a conta existente para manter o saldo original se não for informado
            Conta contaExistente = contaService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

            // Atualizar apenas os campos permitidos
            contaExistente.setTitular(conta.getTitular());
            contaExistente.setNumeroConta(conta.getNumeroConta());
            contaExistente.setTipoConta(conta.getTipoConta());

            // Manter o saldo original (ou atualizar se foi modificado)
            if (conta.getSaldo() != null) {
                contaExistente.setSaldo(conta.getSaldo());
            }

            contaService.salvar(contaExistente);
            return "redirect:/contas";

        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar conta: " + e.getMessage());
            model.addAttribute("conta", conta);
            return "editar-conta";
        }
    }
    @GetMapping("/deletar/{id}")
    public String deletarConta(@PathVariable Long id) {
        contaService.deletar(id); // você precisa implementar isso no service/repository
        return "redirect:/contas"; // volta pra lista
    }

}
