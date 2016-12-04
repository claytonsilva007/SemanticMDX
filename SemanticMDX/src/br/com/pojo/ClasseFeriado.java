package br.com.pojo;

public class ClasseFeriado {
	
	private String nome;
	private String data;
	private String dia;
	private String mes;
	private String ano;
	private String diaDaSemanaOcorrencia;
	private String abrangencia;
	
	public ClasseFeriado(){
		
	}
	
	public ClasseFeriado(String descricao, String numDia, String numMes, String numAno, String diaSemana, String abrangencia){
		super();
		this.nome = descricao;
		this.dia = numDia;
		this.mes = numMes;
		this.ano = numAno;
		this.diaDaSemanaOcorrencia = diaSemana;
		this.abrangencia = abrangencia;
	}

	public ClasseFeriado(String nome, String dia, String mes, String ano, String diaDaSemanaOcorrencia) {
		super();
		this.nome = nome;
		this.dia = dia;
		this.mes = mes;
		this.ano = ano;
		this.diaDaSemanaOcorrencia = diaDaSemanaOcorrencia;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getDiaDaSemanaOcorrencia() {
		return diaDaSemanaOcorrencia;
	}

	public void setDiaDaSemanaOcorrencia(String diaDaSemanaOcorrencia) {
		this.diaDaSemanaOcorrencia = diaDaSemanaOcorrencia;
	}

	public String getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(String abrangencia) {
		this.abrangencia = abrangencia;
	}
}
