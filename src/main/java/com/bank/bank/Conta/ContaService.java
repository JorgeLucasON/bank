package com.bank.bank;


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

    public Conta salvar (Conta conta){
        return contaRepository.save(conta);
    }
    public void deletar(Long id){
        contaRepository.deleteById(id);
    }
    public Conta atualizar(Long id, Conta contaAtualizada) {
        return contaRepository.findById(id)
                .map(conta -> {
                    conta.setTitular(contaAtualizada.getTitular());
                    conta.setNumeroConta(contaAtualizada.getNumeroConta());
                    conta.setTipoConta(contaAtualizada.getTipoConta());
                    conta.setSaldo(contaAtualizada.getSaldo());
                    return contaRepository.save(conta);
                })
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

}
