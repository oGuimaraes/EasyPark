package com.easypark.controllers;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import com.easypark.models.*;

import static java.time.temporal.ChronoUnit.MINUTES;

@Controller
public class EstacionamentoController {
	private EstacionamentoDAO estacionamentoDAO = new EstacionamentoDAO();
	private Estacionamento estacionamentoModel = new Estacionamento();
	private EstadaDAO estadaDAO = new EstadaDAO();

	private static void criaArquivo() throws IOException {
        File f = new File("estada.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        File x = new File("estacionamento.txt");
        if (!x.exists()) {
            x.createNewFile();
        }
        File z = new File("estadasGeral.txt");
        if (!z.exists()) {
            z.createNewFile();
        }
	}

	@RequestMapping("/index")
	public String paginaInicial() {
		//estacionamentoModel.permanenciaTodasEstadas(estadaDAO);
		System.out.println("Media Tempo Permanecido: " + estacionamentoModel.mediaTempoPermanecido());
		System.out.println("Media Arrecadacao Por Hora: " + estacionamentoModel.mediaArrecadacaoHora());
		return "index";
	}
	
	@RequestMapping("/")
	public String inicializaBanco() {
		estacionamentoModel = estacionamentoDAO.instanciaEstacionamento();
		estadaDAO.instanciaEstadas(estacionamentoModel);

		return "index";
	}

	@RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.GET)
	public ModelAndView informacoes(Estacionamento estacionamento) {
		ModelAndView mv = new ModelAndView("cadastroEstabelecimento");
		mv.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		mv.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		mv.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		mv.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		mv.addObject("precoInfo", estacionamentoModel.getValorHora());
		return mv;
	}

	@RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.POST)
	public ModelAndView salvar(@RequestParam("nomeEstabelecimento") String nomeEstabelecimento,
							   @RequestParam("horaAbertura") String horaAbertura,
							   @RequestParam("horaFechamento") String horaFechamento,
			                   @RequestParam("quantidadeVagas") int quantidadeVagas, 
			                   @RequestParam("valorHora") Double valorHora) {
		
		ModelAndView mv = new ModelAndView("redirect:/infoEstabelecimentoCadastrado");
		
		try {
			criaArquivo();	
		} catch (IOException e) {
            e.printStackTrace();
        }
		
		/* Formatação e Data */
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime horaAberturaFormat = LocalTime.parse(horaAbertura, dtf);
		LocalTime horaFechamentoFormat = LocalTime.parse(horaFechamento, dtf);
		estacionamentoModel.setNomeEstabelecimento(nomeEstabelecimento);
		estacionamentoModel.setQuantidadeVagas(quantidadeVagas);
		estacionamentoModel.setValorHora(valorHora);
		estacionamentoModel.setHoraAbertura(horaAberturaFormat);
		estacionamentoModel.setHoraFechamento(horaFechamentoFormat);
		
		ModelAndView modelAndView = new ModelAndView("infoEstabelecimentoCadastrado");
		modelAndView.addObject("nomeEstabelecimento", nomeEstabelecimento);
		modelAndView.addObject("quantidadeVagas", quantidadeVagas);
		modelAndView.addObject("valorHora", valorHora);
		modelAndView.addObject("horaAbertura", horaAberturaFormat);
		modelAndView.addObject("horaFechamento", horaFechamentoFormat);
	
		estacionamentoDAO.add(estacionamentoModel);
		
		/* Printa as informações do estabelecimento instanciado no console */
		System.out.println(estacionamentoModel.toString());
		
		return modelAndView;
	}

	@GetMapping("/entradaVeiculo")
	public ModelAndView entradaForm(Model model) {
		model.addAttribute("veiculo", new Veiculo());
		ModelAndView modelAndView = new ModelAndView("entradaVeiculo");
		modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
		return modelAndView;
		
	}

	@PostMapping("/entradaVeiculo")
	public ModelAndView veiculoEstacionado(@ModelAttribute Veiculo veiculo) {

		ModelAndView modelAndView = new ModelAndView("veiculoEstacionado");
		Map<String, Object> infoEntradaVeiculo = estacionamentoModel.entradaVeiculo(veiculo);

		String placaN = (String) infoEntradaVeiculo.get("placaVeiculo");
		String tipoVeiculoN = (String) infoEntradaVeiculo.get("tipoVeiculo");
		LocalDateTime dataEntradaN = (LocalDateTime) infoEntradaVeiculo.get("dataEntrada");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
		modelAndView.addObject("placaVeiculo", placaN);
		modelAndView.addObject("tipoVeiculo", tipoVeiculoN);
		modelAndView.addObject("dataEntrada", dataEntradaN.format(formatter));
		modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());

		return modelAndView;
	}

	@GetMapping("/saidaVeiculo")
	public ModelAndView saidaForm(Model model) {
		ModelAndView modelAndView = new ModelAndView("saidaVeiculo");
		modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
		model.addAttribute("veiculo", new Veiculo());
		return modelAndView;
	}

	@PostMapping("/saidaVeiculo")
	public ModelAndView veiculoEstacionado(@RequestParam("placa") String placaVeiculoSaindo) {
		ModelAndView modelAndView = new ModelAndView("veiculoSaindo");
		Map<String, Estada> mapEstada = estacionamentoModel.getEstadaList();
		Estada estadaVeiculo = mapEstada.get(placaVeiculoSaindo);
		Map<String, Object> informacoesSaida = estadaVeiculo.saidaVeiculo(estacionamentoModel, placaVeiculoSaindo);
		modelAndView.addObject("placaVeiculo", placaVeiculoSaindo);
		modelAndView.addObject("tempoPermanecido", informacoesSaida.get("tempoPermanecido"));
		modelAndView.addObject("dataHoraEntrada", informacoesSaida.get("dataHoraEntrada"));
		modelAndView.addObject("dataHoraSaida", informacoesSaida.get("dataHoraSaida"));
		modelAndView.addObject("tipoVeiculo", informacoesSaida.get("tipoVeiculo"));
		DecimalFormat formato = new DecimalFormat("#.##");
		modelAndView.addObject("valorAPagar", formato.format(informacoesSaida.get("valorAPagar")));
		modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());

		System.out.println("Imprimindo estada que sera excluida" + estadaVeiculo);

		estadaDAO.delete(estadaVeiculo);

		return modelAndView;
	}

	@RequestMapping(value = "/veiculosEstacionados", method = RequestMethod.GET)
	public ModelAndView veículos(Estacionamento estacionamento) {

		StringBuilder sbVeiculos = estacionamentoModel.exibeVeiculos(estadaDAO);
		StringBuilder barraDeProgressoVeiculos = estacionamentoModel.porcentagemTipoVeiculo(estadaDAO);

		ModelAndView modelAndView = new ModelAndView("veiculosEstacionados");

		modelAndView.addObject("infoVeiculos", sbVeiculos);
		modelAndView.addObject("porcentagemVeiculos", barraDeProgressoVeiculos);
		modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());

		return modelAndView;
	}

	@RequestMapping(value = "/estatisticaDoEstabelecimento", method = RequestMethod.GET)
	public ModelAndView estatistica(Estacionamento estacionamento) {
		ModelAndView modelAndView = new ModelAndView("estatisticaDoEstabelecimento");
		modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
		modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
		modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
		modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
		modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
		DecimalFormat formato = new DecimalFormat("#.##");
		Double horas = estacionamentoModel.mediaTempoPermanecido()/60;
		Double minutos = estacionamentoModel.mediaTempoPermanecido()%60;
		String mediaHorasParaExibir = horas.intValue()+":"+minutos.intValue();
		modelAndView.addObject("mediaTempoPermanecido", mediaHorasParaExibir);
		modelAndView.addObject("mediaArrecadadoHora", "R$ " + formato.format(estacionamentoModel.mediaArrecadacaoHora()));
		modelAndView.addObject("porcentagemCarros", "Carros " + formato.format(estacionamentoModel.porcentagemCarrosGeral(estadaDAO))+"%");
		modelAndView.addObject("porcentagemMotos", "Motos " + formato.format(estacionamentoModel.porcentagemMotosGeral(estadaDAO))+"%");
		modelAndView.addObject("porcentagemCaminhonetes", "Caminhonetes " + formato.format(estacionamentoModel.porcentagemCaminhonetesGeral(estadaDAO))+"%");
		return modelAndView;
	}
}
