package br.com.screenmatch.principal;

import br.com.screenmatch.models.DadosEpisodio;
import br.com.screenmatch.models.DadosSerie;
import br.com.screenmatch.models.DadosTemporada;
import br.com.screenmatch.models.Episodio;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//        System.out.println("Tops 5 episódios: ");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                        .collect(Collectors.toList());
        episodios.forEach(System.out::println);

//        var trechoTitulo = scanner.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if(episodioBuscado.isPresent()) {
//            System.out.println("Episódio encontrado: " +
//                    "Temporada " + episodioBuscado.get().getTemporada() +
//                    " Episódio " + episodioBuscado.get().getTitulo());
//        } else {
//            System.out.println("Episódio não encontrado");
//        }



//        System.out.println("A partir de que ano você deseja ver os episódios?");
//        var ano = scanner.nextInt();
//        LocalDate data = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(data))
//                .forEach(e -> {
//                    System.out.println("Temporada" + e.getTemporada() + " Episodio " + e.getTitulo() +
//                            " lançado em " + e.getDataLancamento().format(formatter));
//                });

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println("Avaliações por temporada: ");
        avaliacoesPorTemporada.forEach((temporada, avaliacao) -> {
            System.out.println("Temporada " + temporada + " avaliação " + avaliacao);
        });


    }

}
