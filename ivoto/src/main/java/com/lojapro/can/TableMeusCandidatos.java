package com.lojapro.can;


public class TableMeusCandidatos {

	private int id;
	private String emailUser;
	private String nome;
	
	public TableMeusCandidatos(){}
	
	public TableMeusCandidatos(int id, String emailUser, String nome) {
		super();
		this.id = id;
		this.emailUser = emailUser;
		this.nome = nome;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmailUser() {
		return emailUser;
	}
	public void setEmailUser(String email) {
		this.emailUser = email;
	}
	
	@Override
	public String toString() {
		return "Candidato [id=" + id + ", nome=" + nome + ", emailUser=" + emailUser
				+ "]";
	}
	
	
	
}
