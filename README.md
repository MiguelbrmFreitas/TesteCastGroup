# TesteCastGroup
Repositório para a avaliação técnica do processo seletivo da Cast Group.

# Especificações técnicas

Mais detalhes serão adicionados aqui nos commits ao longo do desenvolvimento.

## Android


## Servidor

Aplicação em node.js com express com um CRUD para realizar operações de pesquisa, inclusão, alteração e exclusão de Cursos para turmas de formação da Cast Group.

Cada curso possui obrigatoriamente descrição, data de início, data de final e categoria (que tem um model próprio com código e descrição). Opcionalmente pode ser colocado número de alunos.

### Como usar

#### Instalando dependências

```bash
npm install
```

#### Configuração do MongoDB

Foi escolhido o MongoDB como banco de dados. Edite o arquivo /config/default.json para incluir a URL correta do seu banco se for testar localmente.

#### Rodar servidor em modo de desenvolvimento

```bash
npm run server  # Express API: 5001
```

