package br.com.screenmatch.principal;

import br.com.screenmatch.service.ConsumoApi;

import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=7aec031b";
    public void exibemenu(){
        System.out.println("Digite o nome da série: ");
        var serie = scanner.nextLine();

        var json = consumoApi.obterDados(ENDERECO.replace(" ", "+") + serie + API_KEY);

    }

}
