package com.easypark.models;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Estada {
    private LocalDateTime dataEntrada;
    private LocalTime horaEntrada;
    private Veiculo veiculo;
    private LocalDateTime dataSaida;
    private LocalTime horaSaida;
    private long tempoDePermanencia;
    private Double valorEstada;

    public Estada(LocalDateTime dataEntrada, LocalTime horaEntrada, Veiculo veiculo) {
        this.dataEntrada = dataEntrada;
        this.horaEntrada = horaEntrada;
        this.veiculo = veiculo;
    }

    public Estada(LocalDateTime dataEntrada, LocalTime horaEntrada, Veiculo veiculo, LocalDateTime dataSaida, LocalTime horaSaida,
                  long tempoDePermanencia, Double valorEstada) {
        this.dataEntrada = dataEntrada;
        this.veiculo = veiculo;
        this.horaEntrada = horaEntrada;
        this.dataSaida = dataSaida;
        this.horaSaida = horaSaida;
        this.tempoDePermanencia = tempoDePermanencia;
        this.valorEstada = valorEstada;
    }

    public Estada() {
    }

    public Map<String, Object> saidaVeiculo(Estacionamento estacionamento, String placa) {


        Map<String, Object> infoSaidaVeiculo = new HashMap<>();
        Estada estadaVeiculo = estacionamento.getEstadaVeiculo(placa);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
        estadaVeiculo.setDataSaida(LocalDateTime.now());
        estadaVeiculo.setHoraSaida(LocalTime.now().plusHours(2));

        long minutosPermanecidos = estadaVeiculo.getHoraEntrada().until(estadaVeiculo.getHoraSaida(), ChronoUnit.MINUTES);
        estadaVeiculo.setTempoDePermanencia(minutosPermanecidos);
        long horasPermanecidas = 0;
        while (minutosPermanecidos > 59) {
            minutosPermanecidos -= 60;
            horasPermanecidas++;
        }
        if (horasPermanecidas < 0) {
        	horasPermanecidas = horasPermanecidas * (-1);
        }
        if (minutosPermanecidos < 0) {
        	minutosPermanecidos = minutosPermanecidos * (-1);
        }
        estadaVeiculo.calculaValor(horasPermanecidas, minutosPermanecidos, estacionamento.getValorHora());
        LocalTime tempoPermanecido = LocalTime.of((int) horasPermanecidas,(int) minutosPermanecidos);
        
       

        //View
        infoSaidaVeiculo.put("tempoPermanecido", tempoPermanecido);
        infoSaidaVeiculo.put("dataHoraEntrada", estadaVeiculo.getDataEntrada().format(formatter));
        infoSaidaVeiculo.put("dataHoraSaida", estadaVeiculo.getDataSaida().format(formatter));
        infoSaidaVeiculo.put("tipoVeiculo", estadaVeiculo.getVeiculo().getTipoVeiculo());
        infoSaidaVeiculo.put("valorAPagar", estadaVeiculo.getValorEstada());

        estacionamento.getEstadaList().remove(placa);
        EstadaDAO estadaDAO = new EstadaDAO();
        estadaDAO.update(estadaVeiculo);

        return infoSaidaVeiculo;
    }


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
        DecimalFormat formato = new DecimalFormat("#.##");
        valorEstada = Double.valueOf(formato.format(valorEstada).replace(",", "."));
        this.valorEstada = valorEstada;
    }

//    public String calcTempo() {
//
//        int year = (getDataSaida().getYear()) - (getDataEntrada().getYear());
//        int month = (getDataSaida().getMonthValue()) - (getDataEntrada().getMonthValue());
//        int day = (getDataSaida().getDayOfMonth()) - (getDataEntrada().getDayOfMonth());
//        int hour = (getDataSaida().getHour()) - (getDataEntrada().getHour());
//        int minute = (getDataSaida().getMinute()) - (getDataEntrada().getMinute());
//
//        return "" + year + "/" + month + "/" + day + " " + hour + ":" + minute;
//
//    }

    public void calculaValor(long horasPermanecidas, long minutosPermanecidos, Double valorHora) {
        Double valorAPagar;
        Double valorHoraSemMinutos = (valorHora * horasPermanecidas);
        Double valorMinutos = ((valorHora / 60) * minutosPermanecidos);
        valorAPagar = valorHoraSemMinutos + valorMinutos;
        setValorEstada(valorAPagar);
        if (valorAPagar < 0) {
        	valorAPagar = valorAPagar * (-1);
        }
        this.valorEstada = valorAPagar;
    }

    public long getTempoDePermanencia() {
        return tempoDePermanencia;
    }

    public void setTempoDePermanencia(long tempoDePermanencia) {
        this.tempoDePermanencia = tempoDePermanencia;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dataEntrada).append("/").append(this.horaEntrada).append("/")
                .append(this.veiculo.getPlaca()).append("/").append(this.veiculo.getTipoVeiculo()).append("/");
        if (this.getDataSaida() != null) {
            sb.append(this.dataSaida).append("/").append(this.horaSaida).append("/")
                    .append(this.tempoDePermanencia).append("/").append(this.valorEstada);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estada estada = (Estada) o;
        return Objects.equals(veiculo.getPlaca(), estada.veiculo.getPlaca());
    }


    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }


//    @Override
//    public boolean test(Estada estada) {
//
//        return (estada) -> estada.getTempoDePermanencia() > 120;
//    }
}
