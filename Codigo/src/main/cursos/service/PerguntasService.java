package service;

import java.util.ArrayList;
import java.util.HashMap;
import dao.*;
import model.Perguntas;
import spark.Request;
import spark.Response;
import spark.ModelAndView;

public class PerguntasService {

    private static PerguntasDAO perguntasDAO = new PerguntasDAO();

    public PerguntasService() {
    }

    public static ArrayList<Perguntas> mostrarPerguntas(Request req, Response res) {
        ArrayList<Perguntas> perguntasX = new ArrayList<Perguntas>();

        int idAula = Integer.parseInt(req.queryParams("id_aula"));
        Perguntas perguntas[] = perguntasDAO.getPerguntas(); 
       try {
            for (Perguntas pergunta : perguntas) {
                if (pergunta.getId_aula() == idAula) {
                    perguntasX.add(pergunta);
                }
            }
       } catch (Exception e) {
            System.out.println("Sem Perguntas");
       }
        return perguntasX;
    }

    public static Perguntas inserirPergunta(Request req, int id_usuario){
		String idAula = req.queryParams("idAula");
		String descricao = req.queryParams("dsc_pergunta");
		Perguntas perguntaMain = null;
        try {
            perguntaMain = perguntasDAO.inserirPergunta(idAula, descricao, id_usuario);
            return perguntaMain;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return perguntaMain;
	}
    
    public static Boolean excluirPergunta(Request req, int idUsuario){
		String idPergunta = req.queryParams("idPergunta");
        try {
            return perguntasDAO.excluirPergunta(Integer.parseInt(idPergunta), idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
