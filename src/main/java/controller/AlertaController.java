package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testeMaven.com.techmuu.dao.AlertaDAO;
import testeMaven.com.techmuu.model.Alerta;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

  
    @GetMapping
    public ResponseEntity<List<Alerta>> listarTodos() {
        try {
            List<Alerta> alertas = AlertaDAO.listarTodos();
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   
    @GetMapping("/abertos")
    public ResponseEntity<List<Alerta>> listarAbertos() {
        try {
            List<Alerta> alertas = AlertaDAO.listarAbertos();
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/resolvidos")
    public ResponseEntity<List<Alerta>> listarResolvidos() {
        try {
            List<Alerta> alertas = AlertaDAO.listarResolvidos();
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<Alerta> buscarPorId(@PathVariable int id) {
        try {
            Alerta alerta = AlertaDAO.buscarPorId(id);
            if (alerta != null) {
                return ResponseEntity.ok(alerta);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<Alerta>> listarPorNivel(@PathVariable String nivel) {
        try {
            List<Alerta> alertas = AlertaDAO.listarPorNivel(nivel);
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

  
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Alerta>> listarPorTipo(@PathVariable String tipo) {
        try {
            List<Alerta> alertas = AlertaDAO.listarPorTipo(tipo);
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

  
    @PostMapping
    public ResponseEntity<String> inserir(@RequestBody AlertaRequest request) {
        try {
            Alerta novoAlerta = new Alerta(
                0,
                request.getTipoAlerta(),
                request.getDescricao(),
                request.getNivel()
            );
            
            boolean sucesso = AlertaDAO.inserir(novoAlerta, request.getIdVaca());
            
            if (sucesso) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"Alerta criado com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"error\": \"Erro ao criar alerta\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    
    @PutMapping("/{id}/resolver")
    public ResponseEntity<String> marcarComoResolvido(@PathVariable int id) {
        try {
            boolean sucesso = AlertaDAO.marcarComoResolvido(id);
            
            if (sucesso) {
                return ResponseEntity.ok("{\"message\": \"Alerta resolvido com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Alerta não encontrado\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

   
    @PutMapping("/{id}/nivel")
    public ResponseEntity<String> atualizarNivel(
        @PathVariable int id, 
        @RequestBody NivelRequest request
    ) {
        try {
            boolean sucesso = AlertaDAO.atualizarNivel(id, request.getNovoNivel());
            
            if (sucesso) {
                return ResponseEntity.ok("{\"message\": \"Nível atualizado com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Alerta não encontrado\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable int id) {
        try {
            boolean sucesso = AlertaDAO.deletar(id);
            
            if (sucesso) {
                return ResponseEntity.ok("{\"message\": \"Alerta deletado com sucesso\"}");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Alerta não encontrado\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    
    public static class AlertaRequest {
        private String tipoAlerta;
        private String descricao;
        private String nivel;
        private int idVaca;

        public String getTipoAlerta() { return tipoAlerta; }
        public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getNivel() { return nivel; }
        public void setNivel(String nivel) { this.nivel = nivel; }
        
        public int getIdVaca() { return idVaca; }
        public void setIdVaca(int idVaca) { this.idVaca = idVaca; }
    }

    public static class NivelRequest {
        private String novoNivel;

        public String getNovoNivel() { return novoNivel; }
        public void setNovoNivel(String novoNivel) { this.novoNivel = novoNivel; }
    }
}