package com.uniride.service;

import com.uniride.model.Usuario;
import com.uniride.repository.UsuarioRepository;
import com.uniride.service.UsuarioService.ResultadoLogin;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Req. B3 (validação) + A2: o cadastro rejeita dados inválidos na fronteira de confiança.
 * Req. B1 + A1: a senha persistida é um hash BCrypt, nunca o texto claro.
 * Cobre também a autenticação (login com senha correta e incorreta).
 *
 * Observação: o repositório é singleton e o estado persiste entre os testes,
 * por isso cada teste usa um e-mail único.
 */
class UsuarioServiceTest {

    private final UsuarioService service = new UsuarioService();
    private final UsuarioRepository repository = UsuarioRepository.getInstance();

    @Test
    void cadastroRejeitaEmailSemArroba() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Rafael", "emailsemarroba", "senha123", "01/01/2000"));
    }

    @Test
    void cadastroRejeitaSenhaCurta() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Rafael", "rafael@pucpr.edu.br", "123", "01/01/2000"));
    }

    @Test
    void cadastroRejeitaCamposEmBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("", "", "", "01/01/2000"));
    }

    @Test
    void senhaPersistidaComoHashBCryptENaoEmTextoClaro() {
        String email = "hash_" + System.nanoTime() + "@pucpr.edu.br";

        assertTrue(service.cadastrar("Rafael", email, "senha123", "01/01/2000"),
                "Cadastro válido deve retornar true.");

        Optional<Usuario> salvo = repository.findByEmail(email);
        assertTrue(salvo.isPresent(), "Usuário deve ter sido persistido.");
        assertNotEquals("senha123", salvo.get().getSenha(), "A senha não pode ser guardada em texto claro.");
        assertTrue(BCrypt.checkpw("senha123", salvo.get().getSenha()),
                "O hash armazenado deve corresponder à senha original.");
    }

    @Test
    void autenticarAceitaSenhaCorretaERejeitaIncorreta() {
        String email = "login_" + System.nanoTime() + "@pucpr.edu.br";
        service.cadastrar("Rafael", email, "senha123", "01/01/2000");

        ResultadoLogin ok = service.autenticar(email, "senha123");
        assertEquals(ResultadoLogin.Tipo.SUCESSO, ok.tipo, "Senha correta deve autenticar.");

        ResultadoLogin falha = service.autenticar(email, "senhaErrada");
        assertEquals(ResultadoLogin.Tipo.FALHA, falha.tipo, "Senha incorreta não deve autenticar.");
    }
}
