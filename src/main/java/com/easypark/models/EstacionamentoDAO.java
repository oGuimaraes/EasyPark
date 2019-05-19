package com.easypark.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EstacionamentoDAO implements SystemDAO<Estacionamento, String> {

	public EstacionamentoDAO(){
		
	}
	@Override
	public void add(Estacionamento est) {
		Estacionamento novo = est;
		try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estacionamento.txt", true))) {
			String separadorDeLinha = System.getProperty("line.separator");
			buffer_saida.write(novo.getNomeEstabelecimento() + separadorDeLinha);
			buffer_saida.write(novo.getHoraAbertura() + separadorDeLinha);
			buffer_saida.write(novo.getHoraFechamento() + separadorDeLinha);
			buffer_saida.write(novo.getQuantidadeVagas() + separadorDeLinha);
			buffer_saida.write(novo.getValorHora() + separadorDeLinha);
			buffer_saida.flush();

		} catch (Exception e) {
			System.out.println("ERRO ao gravar o Estacionamento " + novo.getNomeEstabelecimento() + "' no disco!");
			e.printStackTrace();
		}
	}

	@Override
	public Estacionamento get(String chave) {
		Estacionamento retorno = null;
		Estacionamento est = null;
		// Veiculo v = null;

		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estacionamento.txt"))) {
			String idSTR;

			while ((idSTR = buffer_entrada.readLine()) != null) {
				est = new Estacionamento();
				est.setNomeEstabelecimento(buffer_entrada.readLine());
				est.setHoraAbertura(LocalTime.parse(buffer_entrada.readLine()));
				est.setHoraFechamento(LocalTime.parse(buffer_entrada.readLine()));
				est.setQuantidadeVagas(Integer.parseInt(buffer_entrada.readLine()));
				est.setValorHora(Double.parseDouble(buffer_entrada.readLine()));
				
				if (chave.equals(est.getNomeEstabelecimento())) {
					retorno = est;
					break;
				}
			}
		} catch (Exception e) {
			System.out
					.println("ERRO a Estacionamento com nome '" + est.getNomeEstabelecimento() + "' do disco rígido!");
			e.printStackTrace();
		}
		return retorno;
	}

	@Override
	public List<Estacionamento> getAll() {
		List<Estacionamento> ests = new ArrayList<Estacionamento>();
		Estacionamento est = null;

		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estacionamento.txt"))) {
			String idSTR;

			while ((idSTR = buffer_entrada.readLine()) != null) {
				est = new Estacionamento();
				est.setNomeEstabelecimento(buffer_entrada.readLine());
				est.setHoraAbertura(LocalTime.parse(buffer_entrada.readLine()));
				est.setHoraFechamento(LocalTime.parse(buffer_entrada.readLine()));
				est.setQuantidadeVagas(Integer.parseInt(buffer_entrada.readLine()));
				est.setValorHora(Double.parseDouble(buffer_entrada.readLine()));
			
				ests.add(est);
			}
		} catch (Exception e) {
			System.out.println("ERRO ao ler os Estacionamentos do disco rígido!");
			e.printStackTrace();
		}
		return ests;
	}

	@Override
	public void update(Estacionamento est) {
		List<Estacionamento> ests = new ArrayList();
		ests.add(est);
		saveToFile(ests);
	}
	
	@Override
	public void delete(Estacionamento est) {
		System.out.println("Nao e possivel deletar o estacionamento");
	}

	private void saveToFile(List<Estacionamento> ests) {
		try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estacionamento.txt", false))) {

			String separadorDeLinha = System.getProperty("line.separator");
			for (Estacionamento est : ests) {
				buffer_saida.write(est.getNomeEstabelecimento() + separadorDeLinha);
				buffer_saida.write(est.getHoraAbertura() + separadorDeLinha);
				buffer_saida.write(est.getHoraFechamento() + separadorDeLinha);
				buffer_saida.write(est.getQuantidadeVagas() + separadorDeLinha);
				buffer_saida.write(est.getEstadaList() + separadorDeLinha);
				buffer_saida.write(est.getValorHora() + separadorDeLinha);
			
				buffer_saida.flush();

			}
		} catch (Exception e) {
			System.out.println("ERRO ao gravar a Estacionamento no disco");
			e.printStackTrace();
		}
	}

}
