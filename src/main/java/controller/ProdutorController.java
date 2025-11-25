package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testeMaven.com.techmuu.dao.ProdutorDAO;
import testeMaven.com.techmuu.model.Produtor;

import java.util.List;

@RestController
@RequestMapping("/api/produtores")
@CrossOrigin(origins = "*")
public class ProdutorController {

 @GetMapping
 public ResponseEntity<List<Produtor>> listarTodos() {
     try {
         List<Produtor> produtores = ProdutorDAO.listarTodos();
         return ResponseEntity.ok(produtores);
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }

 @GetMapping("/{id}")
 public ResponseEntity<Produtor> buscarPorId(@PathVariable int id) {
     try {
         Produtor produtor = ProdutorDAO.buscarPorId(id);
         return produtor != null ? ResponseEntity.ok(produtor) : ResponseEntity.notFound().build();
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }

 @PostMapping
 public ResponseEntity<String> inserir(@RequestBody ProdutorRequest request) {
     try {
         Produtor produtor = new Produtor(0, request.getNome(), request.getEmail(), 
             request.getTelefone(), request.getSenhaHash());
         boolean sucesso = ProdutorDAO.inserir(produtor);
         return sucesso ? 
             ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Produtor inserido\"}") :
             ResponseEntity.badRequest().body("{\"error\": \"Erro ao inserir\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 @PutMapping("/{id}")
 public ResponseEntity<String> atualizar(@PathVariable int id, @RequestBody ProdutorRequest request) {
     try {
         Produtor produtor = new Produtor(id, request.getNome(), request.getEmail(), 
             request.getTelefone(), request.getSenhaHash());
         boolean sucesso = ProdutorDAO.atualizar(produtor);
         return sucesso ? 
             ResponseEntity.ok("{\"message\": \"Produtor atualizado\"}") :
             ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Não encontrado\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 @DeleteMapping("/{id}")
 public ResponseEntity<String> deletar(@PathVariable int id) {
     try {
         boolean sucesso = ProdutorDAO.deletar(id);
         return sucesso ? 
             ResponseEntity.ok("{\"message\": \"Produtor deletado\"}") :
             ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Não encontrado\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 @PostMapping("/login")
 public ResponseEntity<Object> autenticar(@RequestBody LoginRequest request) {
     try {
         Produtor produtor = ProdutorDAO.autenticar(request.getEmail(), request.getSenha());
         if (produtor != null) {
             return ResponseEntity.ok(new LoginResponse(produtor.getId(), produtor.getNome(), 
                 produtor.getEmail(), "produtor"));
         }
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
             .body("{\"error\": \"Credenciais inválidas\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 static class ProdutorRequest {
     private String nome, email, telefone, senhaHash;
     public String getNome() { return nome; }
     public String getEmail() { return email; }
     public String getTelefone() { return telefone; }
     public String getSenhaHash() { return senhaHash; }
 }

 static class LoginRequest {
     private String email, senha;
     public String getEmail() { return email; }
     public String getSenha() { return senha; }
 }

 static class LoginResponse {
     private int id;
     private String nome, email, tipo;
     public LoginResponse(int id, String nome, String email, String tipo) {
         this.id = id; this.nome = nome; this.email = email; this.tipo = tipo;
     }
     public int getId() { return id; }
     public String getNome() { return nome; }
     public String getEmail() { return email; }
     public String getTipo() { return tipo; }
 }
}

