package br.com.alura.screenmatch.model;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@Setter
public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio d) {
        this.temporada = numeroTemporada;
        this.titulo = d.titulo();
        this.numeroEpisodio = d.numero();

        try {
            this.avaliacao = Double.valueOf(d.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(d.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }

    }

    @Override
    public String toString() {
        return "Episodio{" +
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento +
                '}';
    }
}
