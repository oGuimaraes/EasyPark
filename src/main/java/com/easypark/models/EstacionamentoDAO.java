package com.easypark.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class EstacionamentoDAO implements SystemDAO<Estacionamento, String> {

    public EstacionamentoDAO() {

    }

    @Override
    public void add(Estacionamento est) {
        try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estacionamento.txt", true))) {

            buffer_saida.write(est.getNomeEstabelecimento() + ";");
            buffer_saida.write(est.getHoraAbertura() + ";");
            buffer_saida.write(est.getHoraFechamento() + ";");
            buffer_saida.write(est.getQuantidadeVagas() + ";");
            buffer_saida.write(est.getValorHora() + ";");
            buffer_saida.newLine();
            buffer_saida.flush();

        } catch (Exception e) {
            System.out.println("ERRO ao gravar o Estacionamento " + est.getNomeEstabelecimento() + "' no disco!");
            e.printStackTrace();
        }
    }

    @Override
    public Estacionamento get(String chave) {
        Estacionamento est = null;
        try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estacionamento.txt"))) {
            String line;
            while ((line = buffer_entrada.readLine()) != null) {
                if (line.contains(chave)) {
                    String[] atributos = line.split(";");
                    est = new Estacionamento();
                    est.setNomeEstabelecimento(atributos[0]);
                    est.setHoraAbertura(LocalTime.parse(atributos[1]));
                    est.setHoraFechamento(LocalTime.parse(atributos[2]));
                    est.setQuantidadeVagas(Integer.parseInt(atributos[3]));
                    est.setValorHora(Double.parseDouble(atributos[4]));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao recuperar estabelecimento.");
            e.printStackTrace();
        }
        return est;
    }

    @Override
    public List<Estacionamento> getAll() {
        List<Estacionamento> ests = new ArrayList<>();
        Estacionamento est;

        try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estacionamento.txt"))) {

            String line;
            while ((line = buffer_entrada.readLine()) != null) {
                String[] atributos = line.split(";");
                est = new Estacionamento();
                est.setNomeEstabelecimento(atributos[0]);
                est.setHoraAbertura(LocalTime.parse(atributos[1]));
                est.setHoraFechamento(LocalTime.parse(atributos[2]));
                est.setQuantidadeVagas(Integer.parseInt(atributos[3]));
                est.setValorHora(Double.parseDouble(atributos[4]));
                ests.add(est);
            }
        } catch (Exception e) {
            System.out.println("ERRO ao ler os Estacionamentos do disco r�gido!");
            e.printStackTrace();
        }

        return ests;
    }

    @Override
    public void update(Estacionamento n) {

            List<Estacionamento> ests = getAll();
            int index = ests.indexOf(n);
            System.out.println(index);
            if (index != -1) {
                ests.set(index, n);
            }
            saveToFile(ests);

    }

    @Override
    public void delete(Estacionamento est) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Deseja realmente excluir? " + est + "\nDigitar 1 para confirmar");
        int op = sc.nextInt();
        if (op == 1) {
            List<Estacionamento> ests = getAll();
            for (int i = 0; i < ests.size(); i++) {
                if (ests.get(i).equals(est)) {
                    ests.remove(i);
                    break;
                }
            }
            saveToFile(ests);
        }
    }

    private void saveToFile(List<Estacionamento> ests) {
        try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estacionamento.txt", false))) {
            for (Estacionamento est : ests) {
                DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH:mm");
                buffer_saida.write(est.getNomeEstabelecimento() + ";");
                buffer_saida.write(est.getHoraAbertura() + ";");
                buffer_saida.write(est.getHoraFechamento() + ";");
                buffer_saida.write(est.getQuantidadeVagas() + ";");
                buffer_saida.write(est.getValorHora() + ";");
                buffer_saida.newLine();
                buffer_saida.flush();
            }
        } catch (Exception e) {
            System.out.println("ERRO ao gravar a Estacionamento no disco");
            e.printStackTrace();
        }
    }
}
