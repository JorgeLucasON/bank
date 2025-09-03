package com.bank.bank.Conta;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    public Optional<Conta>buscarPorId(Long id){
        return contaRepository.findById(id);
    }

    public Conta salvar(Conta conta) {
        // Validações básicas
        if (conta.getTitular() == null || conta.getTitular().trim().isEmpty()) {
            throw new IllegalArgumentException("Titular é obrigatório");
        }
        if (conta.getNumeroConta() == null || conta.getNumeroConta().trim().isEmpty()) {
            throw new IllegalArgumentException("Número da conta é obrigatório");
        }
        if (conta.getSaldo() == null) {
            conta.setSaldo(0.0);
        }
        return contaRepository.save(conta);
    }
    public Conta atualizar(Long id, Conta contaAtualizada) {
        return contaRepository.findById(id)
                .map(contaExistente -> {
                    contaExistente.setTitular(contaAtualizada.getTitular());
                    contaExistente.setNumeroConta(contaAtualizada.getNumeroConta());
                    contaExistente.setTipoConta(contaAtualizada.getTipoConta());

                    // Atualizar saldo apenas se foi fornecido
                    if (contaAtualizada.getSaldo() != null) {
                        contaExistente.setSaldo(contaAtualizada.getSaldo());
                    }

                    return contaRepository.save(contaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Conta não encontrada com id: " + id));
    }
    public void deletar(Long id) {
        contaRepository.deleteById(id);
    }
}
