# language: pt

Funcionalidade: Autenticação de Usuários
  Como um usuário do sistema
  Eu quero me autenticar na plataforma
  Para que eu possa acessar os recursos protegidos

  Cenário: Login bem-sucedido
    Dado que eu tenho um usuário cadastrado com email "usuario@teste.com" e senha "senha123"
    Quando eu faço uma requisição POST para "/auth/login" com as credenciais corretas
    Então o status da resposta deve ser 200
    E a resposta deve conter um token JWT válido

  Cenário: Login com credenciais inválidas
    Dado que eu tenho um usuário cadastrado com email "usuario@teste.com" e senha "senha123"
    Quando eu faço uma requisição POST para "/auth/login" com a senha incorreta
    Então o status da resposta deve ser 401
    E a resposta deve conter uma mensagem de erro de autenticação

  Cenário: Registro de novo usuário
    Dado que não existe um usuário com email "novo@teste.com"
    Quando eu faço uma requisição POST para "/auth/register" com dados válidos
    Então o status da resposta deve ser 201
    E a resposta deve conter os dados do usuário criado
