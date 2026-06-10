import com.uniride.model.Usuario;
import com.uniride.service.UsuarioService;
import com.uniride.service.UsuarioService.ResultadoLogin;
import com.uniride.service.ViagemService;

/**
 * Teste manual da fronteira de confiança (linha T / Tampering).
 *
 * Chama o UsuarioService DIRETO, sem passar pelo UsuarioController, simulando
 * um ponto de entrada que pula a validação de UX. Dados inválidos devem ser
 * barrados pelo proprio service (IllegalArgumentException) antes de persistir.
 *
 * Cobre dois requisitos:
 *   A1 - cadastro de usuario      -> cadastrar(...)
 *   A2 - habilitacao como motorista -> habilitarComoMotorista(...)
 *
 * Compilar e rodar (a partir da pasta uniride/):
 *   BCRYPT=$(find ~/.m2 -name 'jbcrypt-0.4.jar' | head -1)
 *   javac -cp "target/classes:$BCRYPT" -d tmp tmp/TestTrust.java
 *   java  -cp "tmp:target/classes:$BCRYPT" TestTrust
 *
 * (rode antes um build, ex.: mvn package, para ter target/classes atualizado)
 */
public class TestTrust {

    // ---------- A1: cadastro ----------
    static void tentaCadastro(UsuarioService s, String desc, String nome, String email, String senha) {
        try {
            boolean persistiu = s.cadastrar(nome, email, senha, "01/01/2000");
            if (persistiu) {
                System.out.println("[" + desc + "] OK -> cadastro persistido.");
            } else {
                System.out.println("[" + desc + "] recusado -> e-mail ja cadastrado.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("[" + desc + "] BLOQUEADO pelo service -> " + ex.getMessage());
        }
    }

    // ---------- A2: habilitacao como motorista ----------
    static void tentaMotorista(UsuarioService s, Usuario u, String desc, String cpf, String registro, String venc, boolean confirma) {
        try {
            boolean ok = s.habilitarComoMotorista(u, cpf, registro, venc, confirma);
            if (ok) {
                System.out.println("[" + desc + "] OK -> habilitado. comprovante=" + u.getDadosMotorista().getComprovanteAceite());
            } else {
                System.out.println("[" + desc + "] recusado -> usuario ja e motorista.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("[" + desc + "] BLOQUEADO pelo service -> " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        UsuarioService s = new UsuarioService();

        System.out.println("=== A1: cadastro (fronteira de confianca) ===");
        // Devem ser BLOQUEADOS:
        tentaCadastro(s, "email sem @",  "Ana", "anauni.br",  "123456");
        tentaCadastro(s, "senha curta",  "Ana", "ana@uni.br", "123");
        tentaCadastro(s, "nome vazio",   "",    "ana@uni.br", "123456");
        tentaCadastro(s, "campos nulos", null,  null,         null);
        // Deve persistir:
        tentaCadastro(s, "valido",       "Ana", "ana@uni.br", "123456");
        // Recusado por duplicidade (nao por validacao):
        tentaCadastro(s, "email repetido", "Bia", "ana@uni.br", "654321");

        // Recupera o usuario valido para os testes de habilitacao:
        ResultadoLogin login = s.autenticar("ana@uni.br", "123456");
        Usuario ana = login.usuario;

        System.out.println();
        System.out.println("=== A2: habilitacao como motorista (fronteira de confianca) ===");
        // Devem ser BLOQUEADOS:
        tentaMotorista(s, ana, "cpf vazio",        "",     "CNH123", "01/01/2030", true);
        tentaMotorista(s, ana, "registro vazio",   "111",  "",       "01/01/2030", true);
        tentaMotorista(s, ana, "vencimento nulo",  "111",  "CNH123", null,         true);
        tentaMotorista(s, ana, "sem confirmacao",  "111",  "CNH123", "01/01/2030", false);
        // Deve habilitar (com confirmacao) e gerar comprovante:
        tentaMotorista(s, ana, "valido",           "111",  "CNH123", "01/01/2030", true);
        // Recusado por ja ser motorista:
        tentaMotorista(s, ana, "ja motorista",     "111",  "CNH123", "01/01/2030", true);

        // Outro usuario tentando o MESMO CPF -> deve ser bloqueado:
        s.cadastrar("Bia", "bia@uni.br", "123456", "02/02/2000");
        Usuario bia = s.autenticar("bia@uni.br", "123456").usuario;
        tentaMotorista(s, bia, "cpf duplicado",    "111",  "CNH999", "01/01/2030", true);

        System.out.println();
        System.out.println("=== A3: publicacao de carona (fronteira de confianca) ===");
        ViagemService vs = new ViagemService();
        tentaOferta(vs, ana, "titulo vazio",   "",      "Centro", "Campus", 10);
        tentaOferta(vs, ana, "partida vazia",  "Carona","",       "Campus", 10);
        tentaOferta(vs, ana, "preco negativo", "Carona","Centro", "Campus", -5);
        tentaOferta(vs, ana, "valido",         "Carona","Centro", "Campus", 10);
    }

    // ---------- A3: publicacao de oferta ----------
    static void tentaOferta(ViagemService vs, Usuario u, String desc, String titulo, String partida, String chegada, int preco) {
        try {
            vs.cadastrarOferta(titulo, "desc", partida, chegada, "01/01/2030 08:00", preco, u);
            System.out.println("[" + desc + "] OK -> oferta publicada.");
        } catch (IllegalArgumentException ex) {
            System.out.println("[" + desc + "] BLOQUEADO pelo service -> " + ex.getMessage());
        }
    }
}
