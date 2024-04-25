package service;

import java.util.ArrayList;
import dao.RespostasDAO;
import model.Perguntas;
import model.Respostas;
import spark.Request;
import spark.Response;

public class RespostasService {

    private static RespostasDAO respostasDAO = new RespostasDAO();

    public RespostasService() {
    }

    public static ArrayList<Respostas> mostrarRespostas(Request req, Response res) {
        ArrayList<Respostas> respostasX = new ArrayList<Respostas>();
        try {
            Respostas respostas[] = respostasDAO.getRespostas();
            for (Respostas resposta : respostas) {
                    respostasX.add(resposta);
            }
        } catch (Exception e) {
            System.out.println("Sem Respostas");
        }
        return respostasX;
    }
    public static Respostas inserirResposta(Request req, int idUsuario){
		String idPergunta = req.queryParams("idPergunta");
		String descricao = req.queryParams("resposta");
		Respostas respostaMain = null;
        try {
            respostaMain = respostasDAO.inserirResposta(idPergunta, descricao, idUsuario);
            return respostaMain;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respostaMain;
	}
    public static Boolean excluirResposta(Request req, int idUsuario){
		String idResposta = req.queryParams("idResposta");
        try {
            return respostasDAO.excluirResposta(Integer.parseInt(idResposta), idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
