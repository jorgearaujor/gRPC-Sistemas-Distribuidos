package com.sistemasdistribuidos.grpc.Controller;

import com.sistemasdistribuidos.grpc.proto.*;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherController {

    @GrpcClient("weather")
    private WeatherServiceGrpc.WeatherServiceBlockingStub stub;

    @PostMapping("/cidade")
    public String cadastrar(@RequestParam String nome) {

        CadastrarCidadeRequest request = CadastrarCidadeRequest.newBuilder()
                .setNome(nome)
                .setTemperatura(Math.random() * 30)
                .build();

        return stub.cadastrarCidade(request).getMensagem();
    }

    @GetMapping("/cidades")
    public List<String> listar() {
        return stub.listarCidades(Empty.newBuilder().build()).getCidadesList();
    }

    @GetMapping("/temperatura")
    public double temperatura(@RequestParam String cidade) {

        CidadeRequest request = CidadeRequest.newBuilder()
                .setNome(cidade)
                .build();

        return stub.obterTemperatura(request).getTemperatura();
    }

    @GetMapping("/previsao")
    public List<Previsao> previsao(@RequestParam String cidade) {

        CidadeRequest request = CidadeRequest.newBuilder()
                .setNome(cidade)
                .build();

        return stub.previsaoCincoDias(request).getPrevisoesList();
    }

    @GetMapping("/estatisticas")
    public EstatisticasResponse estatisticas(@RequestParam String cidade) {

        CidadeRequest request = CidadeRequest.newBuilder()
                .setNome(cidade)
                .build();

        return stub.estatisticasClimaticas(request);
    }
}