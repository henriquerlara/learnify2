package service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import dao.AulaDAO;
import dao.CursoDAO;
import model.Aula;
import model.Curso;
import model.Perguntas;
import model.Respostas;
import model.Usuario;
import spark.Request;
import spark.Response;
import spark.ModelAndView;

public class AulaService {

    private static AulaDAO aulaDAO = new AulaDAO();
    private static CursoDAO cursoDAO = new CursoDAO();

    public AulaService() {
    }

    public static boolean comparaAula(Aula aula1, Aula aula2) {
        if (aula1.getIdAula() == aula2.getIdAula()) {
            return true;
        } else {
            return false;
        }
    }

    public static ModelAndView mostrarVideoAula(Request req, Response res, Usuario usuarioX) {
        HashMap<String, Object> model = new HashMap<>();
        
        Aula aulaX = aulaDAO.selecionaAulaX(Integer.parseInt(req.queryParams("id_aula")));
        Curso cursoX = cursoDAO.selecionaCursoX(aulaX.getIdCurso());
        String nomeCurso = cursoX.getNome();

        Aula aulasCurso[] = aulaDAO.getAulasCurso(aulaX.getIdCurso());
        int indiceAula = -1;
        for (int i = 0; i < aulasCurso.length; i++) {
            if (comparaAula(aulaX, aulasCurso[i])) {
                indiceAula = i;
                break;
            }
        }
        if (indiceAula == 0) {
            model.put("aulaAnterior", false);
        } else {
            model.put("aulaAnterior", aulasCurso[indiceAula - 1].getIdAula());
        }
        if (indiceAula == aulasCurso.length - 1) {
            model.put("proximaAula", false);
        } else {
            model.put("proximaAula", aulasCurso[indiceAula + 1].getIdAula());
        }

        ArrayList<Perguntas> perguntasX = new ArrayList<Perguntas>();
        perguntasX = PerguntasService.mostrarPerguntas(req, res);
        ArrayList<Respostas> respostasX = new ArrayList<Respostas>();
        respostasX = RespostasService.mostrarRespostas(req, res);
        try {
            model.put("aula", aulaX);
            model.put("nomeCurso", nomeCurso);
            model.put("perguntas", perguntasX);
            model.put("respostas", respostasX);
            model.put("usuario", usuarioX.getId_usuario());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView(model, "/view/aula.vm");
    }

    public static ModelAndView mostrarAulas(Request req, Response res, boolean dono, Usuario usuario) {
        HashMap<String, Object> model = new HashMap<>();

        int idCurso = Integer.parseInt(req.queryParams("id_curso"));
        Curso cursoX = cursoDAO.selecionaCursoX(idCurso);

        Aula[] aulasX = aulaDAO.getAulasCurso(idCurso);

        try {
            model.put("aulas", aulasX);
            model.put("curso", cursoX);
            model.put("dono", dono);
            model.put("usuario", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView(model, "/view/curso.vm");
    }

    public static ModelAndView mostrarMontarAula(Request req, Response res, Usuario usuario) {
        HashMap<String, Object> model = new HashMap<>();
        int idCurso = Integer.parseInt(req.queryParams("id_curso"));
        try {
            model.put("idCurso", idCurso);
            model.put("usuario", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new ModelAndView(model, "/view/montar_aula.vm");
	}

    public static void inserirAula(Request request, Response response){
		try {
			String base64ThumbURL = request.queryParams("thumbnail");
			// Remova a parte do cabeçalho do Data URL se presente (por exemplo, "data:image/png;base64,")
			String base64Thumb = base64ThumbURL.replaceFirst("data:[^;]*;base64,", "");
			byte[] thumbBytes = Base64.getDecoder().decode(base64Thumb);

            int id_curso = Integer.parseInt(request.queryParams("id_curso"));
			String titulo = request.queryParams("titulo");
			String link = request.queryParams("link");
            link = link.substring(link.indexOf("=") + 1);

			Aula aula = new Aula(id_curso, titulo, thumbBytes, link);
			if (aulaDAO.inserirAula(aula) == true) {
				response.status(201); // 201 Created
			} else {
				response.status(404); // 404 Not found
			}

		} catch (Exception e) {
			response.status(500);
		}
		
	}

    public static void excluirAula(Request request, Response response, int aulaAlvo){
        try {
            if (aulaDAO.excluirAula(aulaAlvo) == true) {
                response.status(201); // 201 Created
            } else {
                response.status(404); // 404 Not found
            }
        } catch (Exception e) {
            response.status(500);
        }
    }

    public static ModelAndView mostrarEditAula(Request req, Response res, Usuario usuario, int aulaAlvo) {
        HashMap<String, Object> model = new HashMap<>();
        Aula aulaX = aulaDAO.selecionaAulaX(aulaAlvo);
        int idCurso = aulaX.getIdCurso();
        int id_usuario = usuario.getId_usuario();

        boolean dono = CursoService.checaUsuarioDono(idCurso, id_usuario);
        if(dono) {
            try {
                model.put("aula", aulaX);
                model.put("dono", dono);
                model.put("idCurso", idCurso);
                model.put("usuario", usuario);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ModelAndView(model, "/view/editar_aula.vm");
        } else {
            HashMap<String, Object> model2 = new HashMap<>();
            return new ModelAndView(model2, "/view/login_form.vm");
        }
        
	}

    public static void atualizarAula(Request request, Response response, int cursoAlvo){
		try {
            String link = request.queryParams("link");
            link = link.substring(link.indexOf("=") + 1);
            String titulo = request.queryParams("titulo");
            int idAula = Integer.parseInt(request.queryParams("idAula"));
            

			byte[] thumbBytes = null;
			String base64ThumbURL = request.queryParams("thumbnail");
			if(base64ThumbURL != ""){
				String base64Thumb = base64ThumbURL.replaceFirst("data:[^;]*;base64,", "");
				thumbBytes = Base64.getDecoder().decode(base64Thumb);
			} else {
				thumbBytes = aulaDAO.selecionaAulaX(idAula).getThumbnail();
			}
			
			Aula aula = new Aula(idAula, cursoAlvo, titulo, thumbBytes, link);
			if (aulaDAO.atualizarAula(aula, idAula) == true) {
				response.status(201); // 201 Created
			} else {
				response.status(404); // 404 Not found
			}
		} catch (Exception e) {
			response.status(500);
		}
	}
}
