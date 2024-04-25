package model;

public class Aula {
    private int idAula;
    private int idCurso;
    private String titulo;
    private byte[] thumbnail;
    private String link;

    public Aula(int idAula, int idCurso, String titulo, byte[] thumbnail, String link) {
        this.idAula = idAula;
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.thumbnail = thumbnail;
        this.link = link;
    }

        public Aula(int idCurso, String titulo, byte[] thumbnail, String link) {
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.thumbnail = thumbnail;
        this.link = link;
    }

    public Aula() {
    }

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    

    public String convertThumbnailBase64() {
		return java.util.Base64.getEncoder().encodeToString(this.thumbnail);
	}
}
