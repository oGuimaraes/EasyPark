package com.easypark.models;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Estada {

    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private Veiculo veiculo;
    private LocalTime tempoDePermanencia;
    private Double valorEstada;

    public Estada(LocalDateTime dataEntrada, LocalTime horaEntrada, Veiculo veiculo) {
        this.dataEntrada = dataEntrada;
        this.horaEntrada = horaEntrada;
        this.veiculo = veiculo;
    }
    
    
    public Estada(LocalDateTime dataEntrada, LocalDateTime dataSaida, LocalTime horaEntrada, LocalTime horaSaida,
			Veiculo veiculo, LocalTime tempoDePermanencia, Double valorEstada) {
		this.dataEntrada = dataEntrada;
		this.dataSaida = dataSaida;
		this.horaEntrada = horaEntrada;
		this.horaSaida = horaSaida;
		this.veiculo = veiculo;
		this.tempoDePermanencia = tempoDePermanencia;
		this.valorEstada = valorEstada;
	}

	public Estada() {
	}

	public Map<String, Object> saidaVeiculo(Estacionamento estacionamento, String placa) {
    	Map<String, Object> infoSaidaVeiculo = new HashMap<>();
    	Estada estadaVeiculo = estacionamento.getEstadaVeiculo(placa);
    	
    	estadaVeiculo.setDataSaida(LocalDateTime.now());
    	estadaVeiculo.setHoraSaida(LocalTime.now().plusHours(2));
    	    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    	
    	System.out.println("HoraEntrada:" + estadaVeiculo.getHoraEntrada());
    	System.out.println("HoraSaida:" + estadaVeiculo.getHoraSaida());
    	
    	Duration diferenca = Duration.between(estadaVeiculo.getHoraEntrada(), estadaVeiculo.getHoraSaida());
    	LocalTime localTimeDiferenca = LocalTime.ofNanoOfDay(diferenca.toNanos());
    	
    	StringBuilder tempoPermanecido = new StringBuilder();
    	tempoPermanecido.append(localTimeDiferenca.getHour() + ":" + localTimeDiferenca.getMinute()).toString();
    	
    	//View
    	infoSaidaVeiculo.put("tempoPermanecido", tempoPermanecido);
    	infoSaidaVeiculo.put("dataHoraEntrada", estadaVeiculo.getDataEntrada().format(formatter));
    	infoSaidaVeiculo.put("dataHoraSaida", estadaVeiculo.getDataSaida().format(formatter));
    	infoSaidaVeiculo.put("tipoVeiculo", estadaVeiculo.getVeiculo().getTipoVeiculo());
    	
    	//Calculo valor
    	infoSaidaVeiculo.put("horasPermanecidas", localTimeDiferenca.getHour());
    	infoSaidaVeiculo.put("minutosPermanecidos", localTimeDiferenca.getMinute());
    	
    	System.out.println("Hora: " + localTimeDiferenca.getHour());
    	System.out.println("Minutos: " + localTimeDiferenca.getMinute());
    	
    	estacionamento.getEstadaList().remove(placa);

		return infoSaidaVeiculo;
    }


//
//	sb.append("Placa: ").append(placaVeiculo).append("\n").append("Data/hora de chegada: ")
//			.append(saida.getDataEntrada().format(formatter)).append("\n>").append("Data/hora de saida: ")
//			.append(saida.getDataSaida().format(formatter)).append("<\n").append("Tempo permanecido: ")
//			.append(localTimeDiferenca.getHour() + ":" + localTimeDiferenca.getMinute()).append("\n")
//			.append("Valor a pagar: R$").append(valorAPagar).append("\n");
//
//	return sb;
//}


    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Double getValorEstada() {
        return valorEstada;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void setValorEstada(Double valorEstada) {
        this.valorEstada = valorEstada;
    }

    public String calcTempo() {

        int year = (getDataSaida().getYear()) - (getDataEntrada().getYear());
        int month = (getDataSaida().getMonthValue()) - (getDataEntrada().getMonthValue());
        int day = (getDataSaida().getDayOfMonth()) - (getDataEntrada().getDayOfMonth());
        int hour = (getDataSaida().getHour()) - (getDataEntrada().getHour());
        int minute = (getDataSaida().getMinute()) - (getDataEntrada().getMinute());

        return "" + year + "/" + month + "/" + day + " " + hour + ":" + minute;

    }
    
	public void calculaValor(int horasPermanecidas, int minutosPermanecidos, Double valorHora) {
		Double valorAPagar;
		System.out.println("ValHora" + valorHora);
		System.out.println("horasPermanecidas" + horasPermanecidas);
		Double valorHoraSemMinutos = (double) (valorHora * horasPermanecidas);
		Double valorMinutos = (double) ((valorHora / 60) * minutosPermanecidos);
		valorAPagar = valorHoraSemMinutos + valorMinutos;
		this.valorEstada = valorAPagar;
	}

    public LocalTime getTempoDePermanencia() {
        return tempoDePermanencia;
    }

    public void setTempoDePermanencia(LocalTime tempoDePermanencia) {
        this.tempoDePermanencia = tempoDePermanencia;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

	@Override
	public String toString() {
		return "Estada [dataEntrada=" + dataEntrada + ", dataSaida=" + dataSaida + ", horaEntrada=" + horaEntrada
				+ ", horaSaida=" + horaSaida + ", veiculo=" + veiculo + ", tempoDePermanencia=" + tempoDePermanencia
				+ "valorEstada=" + valorEstada;
	}
    
    

}
