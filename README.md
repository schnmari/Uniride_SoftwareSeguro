# UniRide

UniRide é um aplicativo de caronas voltado para o público universitário. A proposta é conectar estudantes através de um sistema simples de oferta e solicitação de caronas, facilitando a mobilidade diária até a universidade e incentivando a colaboração entre os alunos.

O projeto foi desenvolvido para a disciplina de Software Seguro (PUCPR), com foco em aplicar boas práticas de segurança no cadastro de usuários, na autenticação e no controle de acesso às caronas.

## Visão geral

A aplicação roda em console e usa persistência em memória (sem banco de dados). As principais funcionalidades são:

- Cadastro e login de usuários, com senha protegida por hash.
- Habilitação de um usuário como motorista, com validação de CPF e termo de aceite.
- Cadastro de ofertas e solicitações de carona.
- Listagem e exclusão das próprias caronas, com verificação de dono.

Do ponto de vista de segurança, o projeto trata pontos como armazenamento seguro de senhas (BCrypt), validação das entradas, bloqueio de conta após tentativas de login malsucedidas, controle de acesso por dono do recurso e registro de log dos eventos sensíveis.

## Tecnologias

- Java 17
- Maven
- jBCrypt 0.4 (hash de senhas)
- JUnit 5 (testes)

## Estrutura

O código segue o padrão MVC, separado por responsabilidade:

```
uniride/
  pom.xml
  src/main/java/com/uniride/
    Main.java          ponto de entrada
    model/             Usuario, Viagem, Motorista
    view/              interação no terminal
    controller/        camada de controle
    service/           regras de negócio e segurança
    repository/        armazenamento em memória
  src/test/java/       testes unitários
```

Observação: o `pom.xml` está dentro da pasta `uniride/`, e não na raiz do repositório.

## Como executar

É necessário ter o Java 17 e o Maven instalados.

```
cd uniride
mvn clean package
java -jar target/uniride-1.0.jar
```

O `mvn clean package` compila o projeto, roda os testes e gera o `.jar` em `target/`.

## Como rodar os testes

```
cd uniride
mvn test
```

Os testes também rodam automaticamente a cada push e pull request, pela configuração em `.github/workflows/ci.yml`.

## Equipe

- Rafael de Faria Sato
- Mariana Schneider Sobrinho
