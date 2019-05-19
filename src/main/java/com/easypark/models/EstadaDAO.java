package com.easypark.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EstadaDAO {

	public EstadaDAO() {

	}

	// @Override
	public static void add(Estada est) {
		Estada nova = est;
		try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estada.txt", true))) {
			String separadorDeLinha = System.getProperty("line.separator");
			buffer_saida.write(nova.getVeiculo().getPlaca() + separadorDeLinha);
			buffer_saida.write(nova.getDataEntrada() + separadorDeLinha);
			buffer_saida.write(nova.getDataSaida() + separadorDeLinha);
			buffer_saida.write(nova.getHoraEntrada() + separadorDeLinha);
			buffer_saida.write(nova.getHoraSaida() + separadorDeLinha);
			buffer_saida.write(nova.getVeiculo().getTipoVeiculo() + separadorDeLinha);
			buffer_saida.write(nova.getTempoDePermanencia() + separadorDeLinha);
			buffer_saida.write(nova.getValorEstada() + separadorDeLinha);
			buffer_saida.flush();

		} catch (Exception e) {
			System.out.println("ERRO ao gravar o Veiculo " + nova.getVeiculo().getPlaca() + "' no disco!");
			e.printStackTrace();
		}
	}

	// @Override
	public static Estada get(String chave) {
		Estada retorno = null;
		Estada est = null;
		Veiculo v = null;

		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estada.txt"))) {
			String idSTR;

			while ((idSTR = buffer_entrada.readLine()) != null) {
				
				v = new Veiculo();
				est = new Estada();
				v.setPlaca(idSTR);
				est.setDataEntrada(LocalDateTime.parse(buffer_entrada.readLine()));
				est.setDataSaida(LocalDateTime.parse(buffer_entrada.readLine()));
				est.setHoraEntrada(LocalTime.parse(buffer_entrada.readLine()));
				est.setHoraSaida(LocalTime.parse(buffer_entrada.readLine()));
				v.setTipoVeiculo(buffer_entrada.readLine());
				est.setTempoDePermanencia(LocalTime.parse(buffer_entrada.readLine()));
				est.setValorEstada(Double.parseDouble(buffer_entrada.readLine()));
				est.setVeiculo(v);

				if (chave.equals(est.getVeiculo().getPlaca())) {
					retorno = est;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("ERRO ao ler o Veiculo de placa '" + est.getVeiculo().getPlaca() + "' do disco rígido!");
			e.printStackTrace();
		}
		return retorno;
	}

	// @Override
	public List<Estada> getAll() {
		List<Estada> ests = new ArrayList<Estada>();
		Estada est = null;
		Veiculo v = null;

		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estada.txt"))) {
			String idSTR;

			while ((idSTR = buffer_entrada.readLine()) != null) {
				est = new Estada();
				v = new Veiculo();
				v.setPlaca(idSTR);
				System.out.println("Data Entrada:" + LocalDateTime.parse(buffer_entrada.readLine()));
				est.setDataEntrada(LocalDateTime.parse(buffer_entrada.readLine()));
				est.setDataSaida(LocalDateTime.parse(buffer_entrada.readLine()));
				est.setHoraEntrada(LocalTime.parse(buffer_entrada.readLine()));
				est.setHoraSaida(LocalTime.parse(buffer_entrada.readLine()));
				v.setTipoVeiculo(buffer_entrada.readLine());
				est.setTempoDePermanencia(LocalTime.parse(buffer_entrada.readLine()));
				est.setValorEstada(Double.parseDouble(buffer_entrada.readLine()));
				est.setVeiculo(v);

				ests.add(est);
				
			}
		} catch (Exception e) {
			System.out.println("ERRO ao ler os Bens de Consumo do disco rígido!");
			e.printStackTrace();
		}
		return ests;
	}

	// @Override
	public void update(Estada est) {
		List<Estada> ests = getAll();
		int index = ests.indexOf(est);
		if (index != -1) {
			ests.set(index, est);
		}
		saveToFile(ests);
	}

	// @Override
	public void delete(Estada est) {
		List<Estada> ests = getAll();
		int index = ests.indexOf(est);
		if (index != -1) {
			ests.remove(index);
		}
		saveToFile(ests);
	}

	private void saveToFile(List<Estada> ests) {
		try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estada.txt", false))) {

			String separadorDeLinha = System.getProperty("line.separator");
			for (Estada est : ests) {
				buffer_saida.write(est.getVeiculo().getPlaca() + separadorDeLinha);
				buffer_saida.write(est.getDataEntrada() + separadorDeLinha);
				buffer_saida.write(est.getDataSaida() + separadorDeLinha);
				buffer_saida.write(est.getHoraEntrada() + separadorDeLinha);
				buffer_saida.write(est.getHoraSaida() + separadorDeLinha);
				buffer_saida.write(est.getVeiculo().getTipoVeiculo() + separadorDeLinha);
				buffer_saida.write(est.getTempoDePermanencia() + separadorDeLinha);
				buffer_saida.write(est.getValorEstada() + separadorDeLinha);
				buffer_saida.flush();

			}
		} catch (Exception e) {
			System.out.println("ERRO ao gravar a Estada no disco!");
			e.printStackTrace();
		}
	}

}
