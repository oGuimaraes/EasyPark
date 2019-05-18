package com.easypark.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
	import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public class Estacionamento {
	private String nomeEstabelecimento;
	private String horaAbertura;
	private String horaFechamento;
	private int quantidadeVagas;
	private int qtdVeiculosEstacionados;
	// private Valores valores;
	private Map<String, Estada> estadaList;
	private Double valorHora;
	private Double valorAPagar;

//    public Estacionamento(String nomeEstabelecimento, String horaAbertura, String horaFechamento, int quantidadeVagas, float valorHora) {
//        this.nomeEstabelecimento = nomeEstabelecimento;
//        this.horaAbertura = horaAbertura;
//        this.horaFechamento = horaFechamento;
//        this.quantidadeVagas = quantidadeVagas;
//        this.valorHora = valorHora;
//        this.estadaList = new HashMap<>();
//    }

	public String getNomeEstabelecimento() {
		return nomeEstabelecimento;
	}

	public void setNomeEstabelecimento(String nome) {
		this.nomeEstabelecimento = nome;
	}

	public String getHoraAbertura() {
		return horaAbertura;
	}

	public void setHoraAbertura(String horaAbertura) {
		this.horaAbertura = horaAbertura;
	}

	public String getHoraFechamento() {
		return horaFechamento;
	}

	public void setHoraFechamento(String horaFechamento) {
		this.horaFechamento = horaFechamento;
	}

	public int getQuantidadeVagas() {
		return quantidadeVagas;
	}

	public void setQuantidadeVagas(int qtdVagas) {
		this.quantidadeVagas = qtdVagas;
	}

	public int getQtdVeiculosEstacionados() {
		return qtdVeiculosEstacionados;
	}

	public void setQtdVeiculosEstacionados(int qtdVeiculosEstacionados) {
		this.qtdVeiculosEstacionados = qtdVeiculosEstacionados;
	}

	public Double getValorHora() {
		return valorHora;
	}

	public void setValorHora(Double valorHora) {
		this.valorHora = valorHora;
	}

	public int calcQtdeVagasLivres() {
		return this.quantidadeVagas - this.qtdVeiculosEstacionados;
	}
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Nome: ").append(this.getNomeEstabelecimento()).append("\n").append("Hora de abertura: ")
				.append(this.horaAbertura).append("\n").append("Hora de fechamento: ").append(this.horaFechamento)
				.append("\n").append("Quantidade de vagas: ").append("\n").append(this.getQuantidadeVagas())
				.append("Valor Hora: ").append(this.getValorHora()).append("\n").append("Estada List:").append(this.getEstadaList()).toString();
		return sb.toString();
	}

	StringBuilder sb = new StringBuilder();
	
	public Map<String, String> entradaVeiculo(Veiculo veiculo) {
		Map<String,String> infoEntradaVeiculo = new HashMap<String,String>();
		
		if (this.estadaList == null) {
			this.estadaList = new HashMap<>();
		}

		LocalDateTime dataEntrada = LocalDateTime.now();
		LocalTime horaEntrada = LocalTime.now();
		
		Estada novaEstada = new Estada(dataEntrada, horaEntrada, veiculo);

		this.getEstadaList().put(veiculo.getPlaca(), novaEstada);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		estadaList.put(novaEstada.getPlaca(), novaEstada);
		
		infoEntradaVeiculo.put("placaVeiculo", veiculo.getPlaca());
		infoEntradaVeiculo.put("tipoVeiculo", veiculo.getTipoVeiculo());
		infoEntradaVeiculo.put("dataEntrada", dataEntrada.format(formatter).toString());
		
		return infoEntradaVeiculo;
	}
	
	public void calculaValor(int horasPermanecidas, int minutosPermanecidos) {
		Double valorAPagar;
		Double valorHoraSemMinutos = (double) (this.getValorHora() * horasPermanecidas);
		Double valorMinutos = (double) ((this.getValorHora() / 60) * minutosPermanecidos);
		valorAPagar = valorHoraSemMinutos + valorMinutos;
		this.valorAPagar = valorAPagar;
	}
	

	
	public Map<String, Estada> getEstadaList() {
		return estadaList;
	}
	
	public Estada getEstadaVeiculo(String placa) {
		return this.estadaList.get(placa);
	}

	public void setEstadaList(Map<String, Estada> estadaList) {
		this.estadaList = estadaList;
	}

}
