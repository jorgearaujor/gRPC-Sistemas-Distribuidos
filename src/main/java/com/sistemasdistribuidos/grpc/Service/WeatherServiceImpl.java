package com.sistemasdistribuidos.grpc.Service;

import com.sistemasdistribuidos.grpc.proto.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.*;

@GrpcService
public class WeatherServiceImpl extends WeatherServiceGrpc.WeatherServiceImplBase {

    private Map<String, List<Double>> dados = new HashMap<>();

    @Override
    public void cadastrarCidade(CadastrarCidadeRequest request,
                                StreamObserver<MensagemResponse> responseObserver) {

        dados.put(request.getNome(),
                List.of(request.getTemperatura(), request.getTemperatura()+2, request.getTemperatura()-2));

        MensagemResponse response = MensagemResponse.newBuilder()
                .setMensagem("Cidade cadastrada!")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void obterTemperatura(CidadeRequest request,
                                 StreamObserver<TemperaturaResponse> responseObserver) {

        double temp = dados.getOrDefault(request.getNome(), List.of(0.0)).get(0);

        TemperaturaResponse response = TemperaturaResponse.newBuilder()
                .setCidade(request.getNome())
                .setTemperatura(temp)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listarCidades(Empty request,
                              StreamObserver<ListaCidadesResponse> responseObserver) {

        ListaCidadesResponse response = ListaCidadesResponse.newBuilder()
                .addAllCidades(dados.keySet())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void previsaoCincoDias(CidadeRequest request,
                                  StreamObserver<PrevisaoResponse> responseObserver) {

        List<Previsao> lista = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            lista.add(Previsao.newBuilder()
                    .setDia("Dia " + i)
                    .setTemperatura(Math.random() * 30)
                    .build());
        }

        PrevisaoResponse response = PrevisaoResponse.newBuilder()
                .addAllPrevisoes(lista)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void estatisticasClimaticas(CidadeRequest request,
                                       StreamObserver<EstatisticasResponse> responseObserver) {

        List<Double> temps = dados.get(request.getNome());

        double media = temps.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double min = temps.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = temps.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        EstatisticasResponse response = EstatisticasResponse.newBuilder()
                .setMedia(media)
                .setMinima(min)
                .setMaxima(max)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}