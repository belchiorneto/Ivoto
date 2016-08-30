package com.lojapro.can;

public class CadastraCidades {
	static DataBaseCidades db;
	public CadastraCidades(int id, String nome, int idestado){
		db = new DataBaseCidades(Principal.getAppContext());
		db.createRegistro(new TableCidades(id, nome, idestado));
	}
}
