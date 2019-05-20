package com.easypark.controllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Map;

import com.easypark.models.*;

@Controller
public class EstacionamentoController {

	private Estacionamento estacionamentoModel = new Estacionamento();

	private EstadaDAO estadaDAO = new EstadaDAO();
	private EstacionamentoDAO estacionamentoDAO = new EstacionamentoDAO();

	public static void criaArquivo() {
		File f = new File("estada.txt");
		if (f.exists())
			f.delete();
		File x = new File("estacionamento.txt");
		if (x.exists())
			x.delete();
	}

	@RequestMapping("/index")
	public String paginaInicial() {
		
		estacionamentoModel.setNomeEstabelecimento("LINDO");
		estacionamentoDAO.update(estacionamentoModel);
		System.out.println(estacionamentoModel.toString());
		return "index";
	}

	@RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.GET)
	public ModelAndView informacoes(Estacionamento estacionamento) {
		ModelAndView mv = new ModelAndView("cadastroEstabelecimento");
		return mv;
	}

	@RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.POST)
	public ModelAndView salvar(@RequestParam("nomeEstabelecimento") String nomeEstabelecimento,
							   @RequestParam("horaAbertura") String horaAbertura,
							   @RequestParam("horaFechamento") String horaFechamento,
			                   @RequestParam("quantidadeVagas") int quantidadeVagas, 
			                   @RequestParam("valorHora") Double valorHora) {
		
		ModelAndView mv = new ModelAndView("redirect:/infoEstabelecimentoCadastrado");
		criaArquivo();
		
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
	public String entradaForm(Model model) {
		model.addAttribute("veiculo", new Veiculo());
		return "entradaVeiculo";
	}

	@PostMapping("/entradaVeiculo")
	public ModelAndView veiculoEstacionado(@ModelAttribute Veiculo veiculo) {

		ModelAndView modelAndView = new ModelAndView("veiculoEstacionado");
		Map<String, Object> infoEntradaVeiculo = estacionamentoModel.entradaVeiculo(veiculo);

		String placaN = (String) infoEntradaVeiculo.get("placaVeiculo");
		String tipoVeiculoN = (String) infoEntradaVeiculo.get("tipoVeiculo");
		LocalDateTime dataEntradaN = (LocalDateTime) infoEntradaVeiculo.get("dataEntrada");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		modelAndView.addObject("placaVeiculo", placaN);
		modelAndView.addObject("tipoVeiculo", tipoVeiculoN);
		modelAndView.addObject("dataEntrada", dataEntradaN.format(formatter));

		estadaDAO.add((Estada) infoEntradaVeiculo.get("novaEstada"));

		System.out.println((Estada) infoEntradaVeiculo.get("novaEstada"));

		return modelAndView;
	}

	@GetMapping("/saidaVeiculo")
	public String saidaForm(Model model) {
		model.addAttribute("veiculo", new Veiculo());
		return "saidaVeiculo";
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

		int minutosPermanecidos = (int) informacoesSaida.get("minutosPermanecidos");
		int horasPermanecidas = (int) informacoesSaida.get("horasPermanecidas");

		Double valorHora = estacionamentoModel.getValorHora();
		estadaVeiculo.calculaValor(horasPermanecidas, minutosPermanecidos, valorHora);

		modelAndView.addObject("valorAPagar", estadaVeiculo.getValorEstada());

		estadaVeiculo.setValorEstada(24.0);
		System.out.println(estadaVeiculo);
		estadaDAO.delete(estadaVeiculo);

		return modelAndView;
	}

	@RequestMapping("/saidaVeiculo")
	public String saidaVeiculo() {
		return "saidaVeiculo";
	}

}
