package com.lojapro.can;


public class TableCidades {

	private int id;
	private String nome;
	private int idestado;
	
	public TableCidades(){}
	
	public TableCidades(int id, String nome, int idestado) {
		super();
		this.id = id;
		this.nome = nome;
		this.idestado = idestado;
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
	public int getIdEstado() {
		return idestado;
	}
	public void setIdEstado(int idestado) {
		this.idestado = idestado;
	}
	
	@Override
	public String toString() {
		return "Cidade [id=" + id + ", nome=" + nome + ", idEstado=" + idestado
				+ "]";
	}
	
	
	
}
