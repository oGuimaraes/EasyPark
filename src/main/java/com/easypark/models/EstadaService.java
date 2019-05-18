package com.easypark.models;

import java.util.Map;

public class EstadaService {


//	public StringBuilder saidaVeiculo() {
//	String placaVeiculo = null; // Input
//
//	int indice = 0;
//
//	// this.estadaList.containsKey(placaVeiculo)
//
//	Estada saida = (Estada) this.estadaList.get(placaVeiculo);
//	Double valorAPagar;
//	Float valorHora = this.getValorHora();
//	// saida.setHoraSaida(LocalTime.now());
//	// saida.setDataSaida(LocalDateTime.now());
//	saida.setHoraSaida(LocalTime.of(23, 48));
//	saida.setDataSaida(LocalDateTime.of(2019, 5, 13, 10, 50));
//	Duration diferenca = Duration.between(saida.getHoraEntrada(), saida.getHoraSaida());
//	LocalTime localTimeDiferenca = LocalTime.ofNanoOfDay(diferenca.toNanos());
//	Double valorHoraSemMinutos = (double) (this.getValorHora() * localTimeDiferenca.getHour());
//	Double valorMinutos = (double) ((this.getValorHora() / 60) * localTimeDiferenca.getMinute());
//	valorAPagar = valorHoraSemMinutos + valorMinutos;
//	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//	StringBuilder sb = new StringBuilder();
//
//	sb.append("Placa: ").append(placaVeiculo).append("\n").append("Data/hora de chegada: ")
//			.append(saida.getDataEntrada().format(formatter)).append("\n>").append("Data/hora de saida: ")
//			.append(saida.getDataSaida().format(formatter)).append("<\n").append("Tempo permanecido: ")
//			.append(localTimeDiferenca.getHour() + ":" + localTimeDiferenca.getMinute()).append("\n")
//			.append("Valor a pagar: R$").append(valorAPagar).append("\n");
//
//	return sb;
//}
}
