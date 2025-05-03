# language: pt

Funcionalidade: Gerenciamento de Aspersores
  Como um administrador do sistema
  Eu quero gerenciar os aspersores do sistema
  Para controlar a irrigação eficientemente

  Cenário: Cadastrar novo aspersor
    Dado que eu estou autenticado no sistema
    Quando eu faço uma requisição POST para "/api/sprinkler" com dados válidos
    Então o status da resposta deve ser 200
    E a resposta deve conter os dados do aspersor criado

  Cenário: Listar todos os aspersores
    Dado que existem aspersores cadastrados no sistema
    Quando eu faço uma requisição GET para "/api/sprinkler"
    Então o status da resposta deve ser 200
    E a resposta deve conter uma lista paginada de aspersores

  Cenário: Deletar um aspersor
    Dado que eu tenho um aspersor cadastrado com ID 1
    Quando eu faço uma requisição DELETE para "/api/sprinkler/1"
    Então o status da resposta deve ser 204
    E o aspersor deve ser removido do sistema
