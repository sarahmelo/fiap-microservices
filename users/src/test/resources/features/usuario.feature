# language: pt

Funcionalidade: Gerenciamento de Usuários
  Como um administrador do sistema
  Eu quero gerenciar os usuários da plataforma
  Para que eu possa manter os dados atualizados

  Cenário: Buscar usuário por ID existente
    Dado que eu tenho um usuário cadastrado no sistema com ID 1
    Quando eu faço uma requisição GET para "/api/user/1"
    Então o código de status da resposta deve ser 200
    E a resposta deve conter os dados do usuário com ID 1

  Cenário: Buscar usuário por ID inexistente
    Dado que não existe um usuário com ID 999
    Quando eu faço uma requisição GET para "/api/user/999"
    Então o código de status da resposta deve ser 404
    E a resposta deve conter uma mensagem de erro

  Cenário: Atualizar dados de um usuário
    Dado que eu tenho um usuário cadastrado no sistema com ID 1
    Quando eu faço uma requisição PUT para "/api/user/1" com novos dados
    Então o código de status da resposta deve ser 200
    E a resposta deve conter os dados atualizados do usuário
