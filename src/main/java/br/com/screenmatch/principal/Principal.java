package br.com.screenmatch.principal;

import br.com.screenmatch.models.DadosEpisodio;
import br.com.screenmatch.models.DadosSerie;
import br.com.screenmatch.models.DadosTemporada;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=7aec031b";
    public void exibemenu(){
        System.out.println("Digite o nome da série: ");
        var serie = scanner.nextLine();

        var json = consumoApi.obterDados(ENDERECO + serie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumoApi.obterDados((ENDERECO + serie.replace(" ", "+") + "&season=" + i + API_KEY));
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
//		temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


    }

}
