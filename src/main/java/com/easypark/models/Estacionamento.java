package com.easypark.models;



import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

import java.util.Map;

@Component
public class Estacionamento {
	private String nomeEstabelecimento;
	private LocalTime horaAbertura;
	private LocalTime horaFechamento;
	private int quantidadeVagas;
	private int qtdVeiculosEstacionados;
	private Map<String, Estada> mapEstadasAtual;
	private Double valorHora;
	private Double valorAPagar;

	public Estacionamento(String nomeEstabelecimento, LocalTime horaAbertura, LocalTime horaFechamento, int quantidadeVagas, Double valorHora) {
		this.nomeEstabelecimento = nomeEstabelecimento;
		this.horaAbertura = horaAbertura;
		this.horaFechamento = horaFechamento;
		this.quantidadeVagas = quantidadeVagas;
		this.valorHora = valorHora;
		this.mapEstadasAtual = new HashMap<>();
	}

	public Estacionamento() {
		this.mapEstadasAtual = new HashMap<>();
	}

	public String getNomeEstabelecimento() {
		return nomeEstabelecimento;
	}

	public void setNomeEstabelecimento(String nome) {
		this.nomeEstabelecimento = nome;
	}

	public LocalTime getHoraAbertura() {
		return horaAbertura;
	}

	public void setHoraAbertura(LocalTime horaAbertura) {
		this.horaAbertura = horaAbertura;
	}

	public LocalTime getHoraFechamento() {
		return horaFechamento;
	}

	public void setHoraFechamento(LocalTime horaFechamento) {
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
		sb.append(this.getNomeEstabelecimento()).append(";").append(this.horaAbertura).append(";")
				.append(this.horaFechamento).append(";").append(this.getQuantidadeVagas()).append(";")
				.append(this.getValorHora()).append(";").append(this.getEstadaList()).toString();
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Estacionamento that = (Estacionamento) o;

		return nomeEstabelecimento.equals(that.nomeEstabelecimento);

	}



	//StringBuilder sb = new StringBuilder();

	public Map<String, Object> entradaVeiculo(Veiculo veiculo) {
		Map<String,Object> infoEntradaVeiculo = new HashMap<>();

		if (this.mapEstadasAtual == null) {
			this.mapEstadasAtual = new HashMap<>();
		}

		/* Formata��o String to DateTime */

		LocalDateTime dataEntrada = LocalDateTime.now();
		LocalTime horaEntrada = LocalTime.now();

		Estada novaEstada = new Estada(dataEntrada, horaEntrada,veiculo);

		this.getEstadaList().put(veiculo.getPlaca(), novaEstada);

		mapEstadasAtual.put(novaEstada.getVeiculo().getPlaca(), novaEstada);
		System.out.println("Veiculos Estacionados"+this.getEstadaList());

		infoEntradaVeiculo.put("placaVeiculo", veiculo.getPlaca());
		infoEntradaVeiculo.put("tipoVeiculo", veiculo.getTipoVeiculo());
		infoEntradaVeiculo.put("dataEntrada", dataEntrada);
		infoEntradaVeiculo.put("horaEntrada", horaEntrada);
		infoEntradaVeiculo.put("novaEstada", novaEstada);

		System.out.println("Exibindo estada no momento da insercao inicial"+novaEstada);

		EstadaDAO estadaDAO = new EstadaDAO();
		estadaDAO.add(novaEstada);

		return infoEntradaVeiculo;
	}

	public void calculaValor(int horasPermanecidas, int minutosPermanecidos) {
		Double valorAPagar;
		System.out.println("Valor hora:" + this.getValorHora());
		Double valorHoraSemMinutos =  this.getValorHora() * horasPermanecidas;
		Double valorMinutos = (this.getValorHora() / 60) * minutosPermanecidos;
		valorAPagar = valorHoraSemMinutos + valorMinutos;
		this.setValorAPagar(valorAPagar);
	}



	public Map<String, Estada> getEstadaList() {
		return mapEstadasAtual;
	}

	public Estada getEstadaVeiculo(String placa) {
		return this.mapEstadasAtual.get(placa);
	}

	public void setEstadaList(Map<String, Estada> estadaList) {
		this.mapEstadasAtual = estadaList;
	}

	public Double getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(Double valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

}
