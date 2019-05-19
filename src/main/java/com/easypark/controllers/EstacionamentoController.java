package com.easypark.controllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

	Estacionamento estacionamentoModel = new Estacionamento();
	EstadaDAO estadaDAO = new EstadaDAO();

	public static void criaArquivo() {
		File f = new File("estada.txt");
		if (f.exists())
			f.delete();
	}

	@RequestMapping("/index")
	public String paginaInicial() {
		Estada estadaAux = estacionamentoModel.getEstadaVeiculo("3");
		estadaAux.setValorEstada(28.0);
		estadaDAO.update(estadaAux);

		return "index";
	}

	@RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.GET)
	public ModelAndView novo(Estacionamento estacionamento) {
		ModelAndView mv = new ModelAndView("cadastroEstabelecimento");
		return mv;
	}

	@RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.POST)
	public ModelAndView salvar(Estacionamento estabelecimento, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("redirect:/cadastroEstabelecimento");
		criaArquivo();
		estacionamentoModel = estabelecimento;
		System.out.println(estacionamentoModel.toString());
		return mv;
	}

//    @GetMapping("/entradaVeiculo")
//    public String entradaForm(Model model) {
//        model.addAttribute("veiculo", new Veiculo());
//        return "entradaVeiculo";
//    }
//
//    @PostMapping("/entradaVeiculo")
//    public ModelAndView veiculoEstacionado(@ModelAttribute Veiculo veiculo) {
//    	ModelAndView modelAndView = new ModelAndView("veiculoEstacionado");
//    	Map<String, String> informacoesEstada = estacionamentoModel.entradaVeiculo(veiculo);
//    	modelAndView.addObject("placaVeiculo", informacoesEstada.get("placaVeiculo"));
//    	modelAndView.addObject("tipoVeiculo", informacoesEstada.get("tipoVeiculo"));
//    	modelAndView.addObject("dataEntrada", informacoesEstada.get("dataEntrada"));
//    	Map<String, Estada> newMap = estacionamentoModel.getEstadaList();
//    	System.out.println(newMap.get(veiculo.getPlaca()));
//    	return modelAndView;
//    }

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
		System.out.println(estadaVeiculo.getNumEstada());
		
		estadaDAO.delete(estadaVeiculo);
		

		return modelAndView;
	}

	@RequestMapping("/saidaVeiculo")
	public String saidaVeiculo() {
		return "saidaVeiculo";
	}

}
