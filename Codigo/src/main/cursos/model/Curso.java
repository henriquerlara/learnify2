package model;

public class Curso {
	private int id_curso;
    private int id_usuario;
    private String categoria;
    private String nome;
    private String descricao;
    private byte[] imagem;
    private byte[] banner;
    
    public Curso(int id_curso, int id_usuario, String categoria, String nome, String descricao, byte[] imagem, byte[] banner) {
		this.id_curso = id_curso;
		this.id_usuario = id_usuario;
		this.categoria = categoria;
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.banner = banner;
	}

    public Curso( int id_usuario, String categoria, String nome, String descricao, byte[] imagem, byte[] banner) {
		this.id_usuario = id_usuario;
		this.categoria = categoria;
		this.nome = nome;
		this.descricao = descricao;
		this.imagem = imagem;
		this.banner = banner;
	}
    
    public Curso() {
    }
    
	public int getId_curso() {
		return id_curso;
	}
	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public byte[] getImagem() {
		return imagem;
	}
	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}
	public byte[] getBanner() {
		return banner;
	}
	public void setBanner(byte[] banner) {
		this.banner = banner;
	}

	public String convertImagemBase64() {
		return java.util.Base64.getEncoder().encodeToString(this.imagem);
	}
	public String convertBannerBase64() {
		return java.util.Base64.getEncoder().encodeToString(this.banner);
	}
}