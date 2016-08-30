package com.lojapro.can;


public class TableUsuario {

	private int id;
	private String nome;
	private String email;
	
	public TableUsuario(){}
	
	public TableUsuario(String title, String author) {
		super();
		this.nome = title;
		this.email = author;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", nome=" + nome + ", email=" + email
				+ "]";
	}
	
	
	
}
