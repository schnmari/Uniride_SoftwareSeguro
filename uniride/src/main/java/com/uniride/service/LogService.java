package com.uniride.service;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registra eventos de auditoria de autenticação (Requisito A1 — linha R/Repudiation
 * da tabela STRIDE). Cada evento fica gravado com data, hora e origem, para que
 * ações na conta possam ser rastreadas e não negadas.
 *
 * Observação: por ser uma aplicação de console local, não existe IP de cliente.
 * Em substituição, registramos o usuário do SO e o host da máquina ("origem").
 */
public class LogService {

    private static final LogService instancia = new LogService();
    private static final Path ARQUIVO = Path.of("logs", "auth.log");
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String origem;

    private LogService() {
        this.origem = detectarOrigem();
    }

    public static LogService getInstance() {
        return instancia;
    }

    public void registrar(String evento, String detalhe) {
        String linha = String.format("%s | %-16s | %s | origem=%s%n",
                LocalDateTime.now().format(FMT), evento, detalhe, origem);
        try {
            Files.createDirectories(ARQUIVO.getParent());
            Files.writeString(ARQUIVO, linha,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            // O logging nunca deve interromper o fluxo de autenticação.
            System.err.println("Falha ao gravar log de auditoria.");
        }
    }

    private String detectarOrigem() {
        String user = System.getProperty("user.name", "desconhecido");
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            host = "host-desconhecido";
        }
        return user + "@" + host;
    }
}
