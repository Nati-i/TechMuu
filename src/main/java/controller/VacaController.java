package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testeMaven.com.techmuu.dao.VacaDAO;
import testeMaven.com.techmuu.model.Animal;

import java.util.List;

public class VacaController {

   
    public ResponseEntity<List<Animal>> listarTodas() {
        try {
            List<Animal> vacas = VacaDAO.listarTodosAnimais();
            return ResponseEntity.ok(vacas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable int id) {
        try {
            Animal vaca = VacaDAO.buscarPorId(id);
            if (vaca != null) {
                return ResponseEntity.ok(vaca);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("/identificador/{identificador}")
    public ResponseEntity<Animal> buscarPorIdentificador(@PathVariable String identificador) {
        try {
            Animal vaca = VacaDAO.buscarPorIdentificador(identificador);
            if (vaca != null) {
                return ResponseEntity.ok(vaca);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @PostMapping
    public ResponseEntity<String> inserir(@RequestBody VacaRequest request) {
        try {
            Animal novaVaca = new Animal(
                0,
                request.getIdentificador(),
                request.getRaca(),
                request.getEstadoReprodutivo()
            );
            
            boolean sucesso = VacaDAO.inserir(novaVaca, request.getIdProdutor());
            
            if (sucesso) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"Vaca inserida com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"error\": \"Erro ao inserir vaca\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable int id, @RequestBody VacaRequest request) {
        try {
            Animal vaca = new Animal(
                id,
                request.getIdentificador(),
                request.getRaca(),
                request.getEstadoReprodutivo()
            );
            
            boolean sucesso = VacaDAO.atualizar(vaca);
            
            if (sucesso) {
                return ResponseEntity.ok("{\"message\": \"Vaca atualizada com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Vaca não encontrada\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable int id) {
        try {
            boolean sucesso = VacaDAO.deletar(id);
            
            if (sucesso) {
                return ResponseEntity.ok("{\"message\": \"Vaca deletada com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Vaca não encontrada\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

   
    @GetMapping("/resumo")
    public ResponseEntity<List<String>> obterResumo() {
        try {
            List<String> resumo = VacaDAO.obterTodasVacas();
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    public static class VacaRequest {
        private String identificador;
        private String raca;
        private String estadoReprodutivo;
        private int idProdutor;

        public String getIdentificador() { return identificador; }
        public void setIdentificador(String identificador) { this.identificador = identificador; }
        
        public String getRaca() { return raca; }
        public void setRaca(String raca) { this.raca = raca; }
        
        public String getEstadoReprodutivo() { return estadoReprodutivo; }
        public void setEstadoReprodutivo(String estadoReprodutivo) { this.estadoReprodutivo = estadoReprodutivo; }
        
        public int getIdProdutor() { return idProdutor; }
        public void setIdProdutor(int idProdutor) { this.idProdutor = idProdutor; }
    }
}