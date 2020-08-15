# TesteCastGroup
Repositório para a avaliação técnica do processo seletivo da Cast Group que envolve um app Android e um backend com conexão com Banco de Dados.

# Especificações técnicas

Implementação de uma API para rotas do backend e de conexão com o banco de dados e de um app Android que se conecte com essa API, apresente dados na tela e receba input dos usuários. Mais detalhes sobre o que foi feito no app e na API nas subseções abaixo.

## Android

Aplicação Android nativa em Java para se conectar com a API descrita abaixo e mostrar uma lista de cursos de formação da Cast Group na tela e receber inputs do usuário para operações de adicionar, editar, remover e filtrar cursos. 

A aplicação segue boas práticas de desenvolvimento orientado a objetos, engenharia de software, tratamento de erros, usabilidade e UX.

### TODO List
- [ ] Conectar com a API feita para este projeto
- [ ] Mostrar lista de cursos na tela
- [ ] Ação de cadastrar curso com descrição, categoria, número de alunos e datas de início e fim
- [ ] Ação de editar curso
- [ ] Ação de remover curso
- [ ] Filtrar cursos por categoria
- [ ] Usar Material Design para os componentes
- [ ] Mostrar alerta de erro para o usuário em caso de ação proibida pelo servidor
- [ ] Aplicação de um ou mais padrões de projeto
- [ ] Mais funcionalidades a incluir e descrever

## Servidor

Aplicação em node.js + Express que implementa um CRUD para realizar operações de pesquisa, inclusão, alteração e exclusão de cursos para turmas de formação da Cast Group.

Foi implementada função de cadastro de categoria (POST) para criar os registros prévios requisitados para o projeto. Cada categoria possui código e descrição

Cada curso possui obrigatoriamente descrição, data de início, data de final e categoria (que tem um model próprio com código e descrição). Opcionalmente pode ser colocado número de alunos.

### TODO List
- [X] Rota POST /categories - cadastrar categoria
- [X] Rota GET /courses - Listar todos os cursos cadastrados
- [X] Rota GET /courses + query param category - Pesquisar por cursos de determinada categoria
- [X] Rota PUT /courses/:id - Editar curso existente
- [X] Rota DELETE /courses/:id - Deletar curso
- [X] Validar se a data de início de um curso não é maior que a data de final
- [X] Validar e não permitir que um curso seja incluído no mesmo período que outro 

### Como usar

#### Instalando dependências

```bash
npm install
```

#### Configuração do MongoDB

Foi escolhido o MongoDB como banco de dados. É um banco de dados orientado a documentos e foi criada uma model <b>Course</b> para representar um curso e uma model <b>Category</b> para representar uma categoria.

Como configuração prévia do banco de dados, foram cadastradas 4 categorias:
![Lista de Categorias](img/Categories_Mongo.png)

Edite o arquivo /config/default.json para incluir a URL correta do seu banco se for testar localmente.

#### Rodar servidor em modo de desenvolvimento

```bash
npm run server  # Express API: 5001
```

