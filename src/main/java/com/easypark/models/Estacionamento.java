package com.easypark.models;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class Estacionamento {
	private String nomeEstabelecimento;
	private LocalTime horaAbertura;
	private LocalTime horaFechamento;
	private int quantidadeVagas;
	private int qtdVeiculosEstacionados = 0;
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

	public Map<String, Object> entradaVeiculo(Veiculo veiculo) {
		Map<String,Object> infoEntradaVeiculo = new HashMap<>();
		veiculo.setContadorDeVezes();

		if (this.mapEstadasAtual == null) {
			this.mapEstadasAtual = new HashMap<>();
		}

		/* Formatacao String to DateTime */

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
		double valorAPagar;
		System.out.println("Valor hora:" + this.getValorHora());
		double valorHoraSemMinutos =  this.getValorHora() * horasPermanecidas;
		double valorMinutos = (this.getValorHora() / 60) * minutosPermanecidos;
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
	
	public double mediaTempoPermanecido() {
		EstadaDAO estadaDAO = new EstadaDAO();
		List<Estada> result = estadaDAO.recuperaEstadasGeral();
		double media = result.stream()
			.filter(t->t.getDataSaida() != null)
			.mapToDouble(Estada::getTempoDePermanencia)
			.average()
			.getAsDouble();
		return media;
	}

    public StringBuilder exibeVeiculos(EstadaDAO estadaDAO) {
		HashMap<Integer, String> infoVeiculo = new HashMap<>();

		StringBuilder veiculos = new StringBuilder();

        List<Estada> veiculosEstacionados = estadaDAO.getAll();
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
		int quantidadeVeiculos = this.getEstadaList().size();

		for (Estada e: veiculosEstacionados) {
			veiculos.append("<div class='row'/>").
					append("<div class='cell'>" + e.getVeiculo().getPlaca() + "</div>").
					append("<div class='cell'>" + e.getVeiculo().getTipoVeiculo() + "</div>").
					append("<div class='cell'>" + e.tempoAtualEstada().format(horaFormatter) + "</div>").append("</div>");
		}
		return veiculos;
    }

    public void permanenciaTodasEstadas(EstadaDAO estadaDAO) {
        List<Estada> estadas = estadaDAO.recuperaEstadasGeral();
        int tempoTotalEstadas = 0;
        float contadorEstadas = 0;
        for (int i = 0; i < estadas.size(); i++) {
            tempoTotalEstadas += estadas.get(i).getTempoDePermanencia();
            contadorEstadas++;
        }
        System.out.println("Tempo medio permanecido: "+ tempoTotalEstadas/contadorEstadas);
    }

	public StringBuilder porcentagemTipoVeiculo(EstadaDAO estadaDAO) {
		List<Estada> result = estadaDAO.getAll();
		Map<String, Double> porcentagensVeiculos = new HashMap<>();

		double porcentagemCarros = (double)(result.stream().filter(t-> t.getVeiculo().getTipoVeiculo().equals("Carro")).count())/result.size()*100;
		double porcentagemMotos = (double)(result.stream().filter(t-> t.getVeiculo().getTipoVeiculo().equals("Moto")).count())/result.size()*100;
		double porcentagemCaminhonete = (double)(result.stream().filter(t-> t.getVeiculo().getTipoVeiculo().equals("Caminhonete")).count())/result.size()*100;

		StringBuilder barraProgresso = new StringBuilder();

		DecimalFormat df = new DecimalFormat("#,###.0");

		barraProgresso.append("<div class='progress-bar progress-bar-success' role='progressbar' style='width:" + porcentagemCarros + "%'> Carro (" + df.format(porcentagemCarros) + "%) </div>")
				.append("<div class='progress-bar progress-bar-warning' role='progressbar' style='width:" + porcentagemMotos + "%'> Moto (" + df.format(porcentagemMotos) + "%) </div>")
				.append("<div class='progress-bar progress-bar-danger' role='progressbar' style='width:" + porcentagemCaminhonete + "%'> Caminhonete (" + df.format(porcentagemCaminhonete) + "%) </div>");

		return barraProgresso;
	}

	public double mediaArrecadacaoHora() {
		EstadaDAO estadaDAO = new EstadaDAO();
		List<Estada> result = estadaDAO.recuperaEstadasGeral();
		double media = result.stream()
				.filter(t -> t.getDataSaida() != null)
				.mapToDouble(Estada::getValorEstada)
				.average().getAsDouble();

		double horasFuncionamento = (double)(MINUTES.between(this.getHoraAbertura(), this.getHoraFechamento())/60);
		return (media/horasFuncionamento);
	}

}
