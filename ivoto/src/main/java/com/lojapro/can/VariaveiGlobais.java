package com.lojapro.can;

import android.app.Application;

public class VariaveiGlobais extends Application {
	private String id_android;
    private String id_candidato;
    private String id_cidade;
    public String getIdAndroid() {
        return id_android;
    }
    public String getIdCandidato() {
        return id_candidato;
    }
    public String getIdCidade() {
        return id_cidade;
    }
    public void setIdAndroid(String aIdAndroid) {
        id_android = aIdAndroid;
    }
    public void setIdCandidato(String aIdCandidato) {
        id_candidato = aIdCandidato;
    }
    public void setIdCidade(String aIdCidade) {
        id_cidade = aIdCidade;
    }
}
