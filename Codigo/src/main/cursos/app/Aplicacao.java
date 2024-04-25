package app;

import static spark.Spark.*;

import java.util.HashMap;

import com.google.gson.JsonObject;

import model.Perguntas;
import model.Respostas;
import model.Usuario;
import service.AulaService;
import service.CursoService;
import service.PerguntasService;
import service.RespostasService;
import service.UsuarioService;
import spark.ModelAndView;
import spark.Session;
import spark.template.velocity.VelocityTemplateEngine;

public class Aplicacao {
	
	public static void main(String[] args) {
    	VelocityTemplateEngine engine = new VelocityTemplateEngine();
        staticFiles.location("/public");
        port(6788);
        
        before((request, response) -> {
            Session session = request.session();
        });


        //inicio das rotas de usuario
        get("/home", (request, response) ->{
            Session session = request.session();
            boolean logado = session.attribute("usuario") != null;
            Usuario usuario_atual =  session.attribute("usuario");

            if(logado){
                return CursoService.mostrarCursos(request, response, usuario_atual);
            }else{
                // fazer um objeto modelandview para retirar repetição de codigo
                HashMap<String, Object> model = new HashMap<>();
                return modelAndView(model, "/view/login_form.vm");
            }
        }, engine);

        // chama a pagina login_form
        get("/login", (request, response) ->{
            HashMap<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "/view/login_form.vm");
        }, engine);
        // pega o formulario de login e continua, caso esteja certo, para o home
        post("/login", (request, response) ->{
            Session session = request.session();
            Usuario usuario = UsuarioService.verificaUsuario(request);
            if(usuario!=null){
                session.attribute("usuario", usuario);
                response.redirect("/home");
                return "Login bem-sucedido";
            }else {
                response.status(401);
                return "Autenticação falhou";
            }
        });

        get("/logout", (request, response) -> {
            request.session().removeAttribute("usuario");
            response.redirect("/login");
            return "Retirado da rota ok";
        });

        post("/register", (request, response) ->{
            if(UsuarioService.inserirUsuario(request)){
                return "registro bem-sucedido";
            }else {
                response.status(401);
                return "Autenticação falhou";
            }
        });
        get("/usuario", (request, response) -> {
            Session session = request.session();
            Usuario usuario_atual = session.attribute("usuario");
            
            int id = Integer.parseInt(request.queryParams("id_usuario"));
        
            if (usuario_atual != null && usuario_atual.getId_usuario() == id) {
                return UsuarioService.mostrarUsuarioEdicao(request, response, id);
            }
            else {
                response.redirect("/home");
                return null;
            }
        }, engine);
        post("/excluir_usuario", (request, response) -> {
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            int id=Integer.parseInt(request.queryParams("id_usuario"));
            
            if (usuario_atual != null) {
                UsuarioService.excluirUsuario(request, response, id);
                response.redirect("/login");
                return "Usuario Excluido";
            } else {
                response.status(401);
                return "Erro";
            }
        });
        post("/atualizar_usuario", (request, response) -> {
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            String email=request.queryParams("email");
            String senha=request.queryParams("senha");
            int id=Integer.parseInt(request.queryParams("id_usuario"));
            
            if (usuario_atual != null) {
                UsuarioService.atualizarUsuario(request, response, id, email, senha);
                return "Usuario Alterado";
            } else {
                response.status(401);
                return "Erro";
            }
        });

        //fim das rotas de usuario


        //inicio das rotas de pergunta e resposta
        post("/insert_pergunta",(request, response) ->{
            Session session = request.session();
            Usuario usuario =  session.attribute("usuario");

            if(usuario != null){
                int id_usuario = usuario.getId_usuario();
                Perguntas pergunta = PerguntasService.inserirPergunta(request, id_usuario);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("id_aula",pergunta.getId_aula());
                jsonResponse.addProperty("descricao",pergunta.getDescricao());
                jsonResponse.addProperty("id_pergunta",pergunta.getId_pergunta());
                jsonResponse.addProperty("id_usuario",pergunta.getId_usuario());
                return jsonResponse;
            }else{
                response.status(401);
                return "Erro ao inserir rota";
            }
        });
        post("/excluir_resposta",(request, response) ->{
            Session session = request.session();
            Usuario usuario =  session.attribute("usuario");

            if (usuario != null) {
                int id_usuario = usuario.getId_usuario();
                Boolean resposta = RespostasService.excluirResposta(request, id_usuario);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("boo_excluido", resposta);
                return jsonResponse;
            } else {
                response.status(401);
                return "Erro ao inserir rota";
            }
        });
        
        post("/excluir_pergunta",(request, response) ->{
            Session session = request.session();
            Usuario usuario =  session.attribute("usuario");

            if (usuario != null) {
                int id_usuario = usuario.getId_usuario();
                Boolean resposta = PerguntasService.excluirPergunta(request, id_usuario);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("boo_excluido", resposta);
                return jsonResponse;
            } else {
                response.status(401);
                return "Erro ao inserir rota";
            }
        });

        post("/insert_resposta",(request, response) ->{
            Session session = request.session();
            Usuario usuario =  session.attribute("usuario");

            if (usuario != null) {
                int id_usuario = usuario.getId_usuario();
                Respostas resposta = RespostasService.inserirResposta(request, id_usuario);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("id_pergunta",resposta.getId_pergunta());
                jsonResponse.addProperty("id_resposta",resposta.getId_resposta());
                jsonResponse.addProperty("descricao",resposta.getDescricao());
                jsonResponse.addProperty("id_usuario",resposta.getId_usuario());
                return jsonResponse;
            } else {
                response.status(401);
                return "Erro ao inserir rota";
            }
        });
        //fim das rotas de pergunta e resposta


        //inicio das rotas de curso
        get("/curso", (request, response) ->{
            Session session = request.session();
            boolean logado = session.attribute("usuario") != null;
            int id_curso = Integer.parseInt(request.queryParams("id_curso"));
            Usuario usuario_atual = session.attribute("usuario");
            int id_usuario = usuario_atual.getId_usuario();
            boolean dono = CursoService.checaUsuarioDono(id_curso, id_usuario);
            if(logado){
                return AulaService.mostrarAulas(request, response, dono, usuario_atual);
            }else{
                HashMap<String, Object> model = new HashMap<>();
                return modelAndView(model, "/view/login_form.vm");
            }
        }, engine);

        get("/montar-curso", (request, response) -> {
            Session session = request.session();
            Usuario usuario_atual = session.attribute("usuario");
            return CursoService.mostrarMontarCurso(request, response, usuario_atual);
        }, engine);

        post("/inserir_curso", (request, response) -> {
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            
            if (usuario_atual != null) {
                int usuarioId = usuario_atual.getId_usuario();
                CursoService.inserirCurso(request, response, usuarioId);
                return "Curso inserido com sucesso";
            } else {
                response.status(401);
                return "Usuário não está na sessão";
            }
        });

        get("/editar_curso", (request, response) -> {
            Session session = request.session();
            boolean logado = session.attribute("usuario") != null;
            int id_curso = Integer.parseInt(request.queryParams("id_curso"));
            Usuario usuario_atual = session.attribute("usuario");
            int id_usuario = usuario_atual.getId_usuario();
            boolean dono = CursoService.checaUsuarioDono(id_curso, id_usuario);
            if(logado){
                return CursoService.mostrarEditarCurso(request, response, id_curso, dono, usuario_atual);
            }else{
                HashMap<String, Object> model = new HashMap<>();
                return modelAndView(model, "/view/curso.vm");
            }
        }, engine);

        post("/excluir_curso", (request, response) -> {
            int cursoAlvo = Integer.parseInt(request.queryParams("idCurso"));
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            
            if (usuario_atual != null && CursoService.checaUsuarioDono(cursoAlvo, usuario_atual.getId_usuario())) {
                CursoService.excluirCurso(request, response, cursoAlvo);
                return "Curso excluido com sucesso";
            } else {
                response.status(401);
                return "Usuário não está é dono do curso";
            }
        });

        post("/atualizar_curso", (request, response) -> {
            int cursoAlvo = Integer.parseInt(request.queryParams("idCurso"));
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            int id_usuario = usuario_atual.getId_usuario();
            
            if (usuario_atual != null && CursoService.checaUsuarioDono(cursoAlvo, id_usuario)) {
                CursoService.atualizarCurso(request, response, id_usuario);
                return "Curso atualizado com sucesso";
            } else {
                response.status(401);
                return "Usuário não é dono do curso";
            }
        });
        //fim das rotas de curso


        //inicio das rotas de aula
        get("/aula", (request, response) ->{
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            boolean logado = session.attribute("usuario") != null;
            if(logado){
                return AulaService.mostrarVideoAula(request, response, usuario_atual);
            }else{
                HashMap<String, Object> model = new HashMap<>();
                return modelAndView(model, "/view/login_form.vm");
            }
        }, engine);

        post("/excluir_aula", (request, response) -> {
            int aulaAlvo = Integer.parseInt(request.queryParams("idAula"));
            int cursoAlvo = Integer.parseInt(request.queryParams("idCurso"));
            
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            
            if (usuario_atual != null && CursoService.checaUsuarioDono(cursoAlvo, usuario_atual.getId_usuario())) {
                AulaService.excluirAula(request, response, aulaAlvo);
                return "Aula excluida com sucesso";
            } else {
                response.status(401);
                return "Usuário não está é dono do curso";
            }
        });

        get("/montar-aula", (request, response) -> {
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            return AulaService.mostrarMontarAula(request, response, usuario_atual);
        }, engine);

        post("/inserir_aula", (request, response) -> {
            try {
                AulaService.inserirAula(request, response);
                return "Aula inserida com sucesso";
            } catch (Exception e) {
                response.status(401);
                return "Falha ao inserir aula";
            }
        });

        get("/editar_aula", (request, response) -> {
            Session session = request.session();
            boolean logado = session.attribute("usuario") != null;
            int aulaAlvo = Integer.parseInt(request.queryParams("id_aula"));
            Usuario usuario_atual = session.attribute("usuario");
            
            if(logado){
                return AulaService.mostrarEditAula(request, response, usuario_atual, aulaAlvo);
            }else{
                HashMap<String, Object> model = new HashMap<>();
                return modelAndView(model, "/view/home.vm");
            }
        }, engine);

        post("/atualizar_aula", (request, response) -> {
            int cursoAlvo = Integer.parseInt(request.queryParams("idCurso"));
            Session session = request.session();
            Usuario usuario_atual =  session.attribute("usuario");
            int id_usuario = usuario_atual.getId_usuario();
            
            if (usuario_atual != null && CursoService.checaUsuarioDono(cursoAlvo, id_usuario)) {
                AulaService.atualizarAula(request, response, cursoAlvo);
                return "Aula atualizada com sucesso";
            } else {
                response.status(401);
                return "Usuário não é dono do curso";
            }
        });
        //fim das rotas de aula
    }
}