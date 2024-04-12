package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private Scanner input = new Scanner(System.in);
    public void exibeMenu() {


        System.out.println("Digite o nome da série para a busca");
        var nomeSerie = input.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace("", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);


        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i<=dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace("", "+") +"&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));



//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//        nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N")).forEach(System.out::println);

//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
//        System.out.println("\nTop 10 episódios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);


        // Optional é um contêiner que guarda o objeto caso ele exista.Ele pode conter um valor único ou nenhum valor.
        System.out.println("Digite um trecho do título do episódio");
        var trechoTitulo = input.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }



//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = input.nextInt();
//        input.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento()
//                ));
//
//


        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
    }
}
