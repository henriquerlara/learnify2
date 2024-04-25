package service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

import dao.CursoDAO;
import dao.AulaDAO;
import model.*;
import spark.Request;
import spark.Response;
import spark.ModelAndView;

public class CursoService {

	private static AulaDAO aulaDAO = new AulaDAO();
	private static CursoDAO cursoDAO = new CursoDAO();

	public CursoService() {
	}

	public static ModelAndView mostrarCursos(Request req, Response res, Usuario usuario) {
//		  insertTeste(req, res);
		HashMap<String, Object> model = new HashMap<>();
		Curso[] cursos = cursoDAO.getCursos();

		try {
			model.put("cursos", cursos);
			model.put("usuario", usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView(model, "/view/home.vm");
	}

	public static ModelAndView mostrarMontarCurso(Request req, Response res, Usuario usuario) {
		HashMap<String, Object> model = new HashMap<>();
		try {
			model.put("usuario", usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView(model, "/view/montar_curso.vm");
	}

	public static void inserirCurso(Request request, Response response, int usuario_id){
		try {
			String base64ImageURL = request.queryParams("imagem");
			String base64BannerURL = request.queryParams("banner");
			// Remova a parte do cabeçalho do Data URL se presente (por exemplo, "data:image/png;base64,")
			String base64Imagem = base64ImageURL.replaceFirst("data:[^;]*;base64,", "");
			byte[] imagemBytes = Base64.getDecoder().decode(base64Imagem);

			String base64Banner = base64BannerURL.replaceFirst("data:[^;]*;base64,", "");
			byte[] bannerBytes = Base64.getDecoder().decode(base64Banner);

			int id_usuario = usuario_id;
			String categoria = request.queryParams("categoria");
			String nome = request.queryParams("nome");
			String descricao = request.queryParams("descricao");

			Curso curso = new Curso(id_usuario, categoria, nome, descricao, imagemBytes, bannerBytes);
			if (cursoDAO.inserirCurso(curso) == true) {
				response.status(201); // 201 Created
			} else {
				response.status(404); // 404 Not found
			}

		} catch (Exception e) {
			response.status(500);
		}
		
	}

	public static ModelAndView mostrarEditarCurso(Request req, Response resp, int idCurso, boolean dono, Usuario usuario){
        HashMap<String, Object> model = new HashMap<>();
        Aula[] aulas = aulaDAO.getAulas();
        ArrayList<Aula> aulasX = new ArrayList<Aula>();

        Curso cursoX = null;
        cursoX = cursoDAO.selecionaCursoX(idCurso);
        for (Aula aula : aulas) {
            if (aula.getIdCurso() == idCurso) {
                aulasX.add(aula);
            }
        }

        try {
            model.put("aulas", aulasX);
            model.put("curso", cursoX);
			model.put("dono", dono);
			model.put("idCurso", idCurso);
			model.put("usuario", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView(model, "/view/editar_curso.vm");
		
	}

	public static void excluirCurso(Request request, Response response, int idCurso){
		try {
			cursoDAO.excluirCurso(idCurso);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checaUsuarioDono(int id_curso, int id_usuario){
		boolean bool = false;
        try {
            bool = cursoDAO.checaUsuarioDono(id_curso, id_usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return bool;
    }

	public static void atualizarCurso(Request request, Response response, int usuario_id){
		try {
			int id_usuario = usuario_id;
			String categoria = request.queryParams("categoria");
			String nome = request.queryParams("nome");
			String descricao = request.queryParams("descricao");
			int id_cursoAlvo = Integer.parseInt(request.queryParams("idCurso"));

			byte[] imagemBytes = null;
			String base64ImageURL = request.queryParams("imagem");
			if(base64ImageURL != ""){
				String base64Imagem = base64ImageURL.replaceFirst("data:[^;]*;base64,", "");
				imagemBytes = Base64.getDecoder().decode(base64Imagem);
			} else {
				imagemBytes = cursoDAO.selecionaCursoX(id_cursoAlvo).getImagem();
			}
			
			byte[] bannerBytes = null;
			String base64BannerURL = request.queryParams("banner");
			if(base64BannerURL != ""){
				String base64Banner = base64BannerURL.replaceFirst("data:[^;]*;base64,", "");
				bannerBytes = Base64.getDecoder().decode(base64Banner);
			} else {
				bannerBytes = cursoDAO.selecionaCursoX(id_cursoAlvo).getBanner();
			}
			
			Curso curso = new Curso(id_usuario, categoria, nome, descricao, imagemBytes, bannerBytes);
			if (cursoDAO.atualizarCurso(curso, id_cursoAlvo) == true) {
				response.status(201); // 201 Created
			} else {
				response.status(404); // 404 Not found
			}
		} catch (Exception e) {
			response.status(500);
		}
	}
}