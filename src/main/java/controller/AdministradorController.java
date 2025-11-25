package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testeMaven.com.techmuu.dao.AdministradorDAO;
import testeMaven.com.techmuu.model.Administrador;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

 @GetMapping
 public ResponseEntity<List<Administrador>> listarTodos() {
     try {
         List<Administrador> admins = AdministradorDAO.listarTodos();
         return ResponseEntity.ok(admins);
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }

 @GetMapping("/{id}")
 public ResponseEntity<Administrador> buscarPorId(@PathVariable int id) {
     try {
         Administrador admin = AdministradorDAO.buscarPorId(id);
         return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }

 @GetMapping("/nivel/{nivel}")
 public ResponseEntity<List<Administrador>> listarPorNivel(@PathVariable String nivel) {
     try {
         List<Administrador> admins = AdministradorDAO.listarPorNivel(nivel);
         return ResponseEntity.ok(admins);
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }

 @PostMapping
 public ResponseEntity<String> inserir(@RequestBody AdminRequest request) {
     try {
         Administrador admin = new Administrador(0, request.getNome(), request.getEmail(), 
             request.getTelefone(), request.getNivelAcesso());
         boolean sucesso = AdministradorDAO.inserir(admin, request.getSenhaHash());
         return sucesso ? 
             ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Administrador inserido\"}") :
             ResponseEntity.badRequest().body("{\"error\": \"Erro ao inserir\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 @PutMapping("/{id}")
 public ResponseEntity<String> atualizar(@PathVariable int id, @RequestBody AdminRequest request) {
     try {
         Administrador admin = new Administrador(id, request.getNome(), request.getEmail(), 
             request.getTelefone(), request.getNivelAcesso());
         boolean sucesso = AdministradorDAO.atualizar(admin);
         return sucesso ? 
             ResponseEntity.ok("{\"message\": \"Administrador atualizado\"}") :
             ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Não encontrado\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 @DeleteMapping("/{id}")
 public ResponseEntity<String> deletar(@PathVariable int id) {
     try {
         boolean sucesso = AdministradorDAO.deletar(id);
         return sucesso ? 
             ResponseEntity.ok("{\"message\": \"Administrador deletado\"}") :
             ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Não encontrado\"}");
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body("{\"error\": \"" + e.getMessage() + "\"}");
     }
 }

 static class AdminRequest {
     private String nome, email, telefone, nivelAcesso, senhaHash;
     public String getNome() { return nome; }
     public String getEmail() { return email; }
     public String getTelefone() { return telefone; }
     public String getNivelAcesso() { return nivelAcesso; }
     public String getSenhaHash() { return senhaHash; }
 }
}