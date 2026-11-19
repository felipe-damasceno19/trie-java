# Árvore trie em Java

## Estrutura e inserção

### O que é uma trie e para que serve?

A trie é uma árvore que organiza palavras por suas letras. Palavras que começam da mesma forma compartilham uma parte do caminho. Esse começo em comum é chamado de prefixo.

Por exemplo, `casa` e `casaco` compartilham o caminho das letras `c`, `a`, `s` e `a`. Para guardar `casaco`, continuamos esse caminho com as letras `c` e `o`.

```text
raiz → c → a → s → a ★ → c → o ★

★ indica onde uma palavra cadastrada termina.
```

A principal utilidade da trie é trabalhar com prefixos, como em um autocompletar. Para encontrar palavras que começam com `cas`, chegamos diretamente a esse caminho e exploramos os ramos abaixo dele, sem percorrer as palavras dos outros ramos.

### Organização do projeto

A estrutura e os algoritmos são implementados em Java, usando objetos e arrays, sem uma trie pronta ou coleções prontas para armazenar os nós e as sugestões.

A classe `NoTrie` representa cada nó. A classe `Trie` reúne as operações da árvore. A classe `Main` contém os testes. Essa separação organiza as responsabilidades. Os atributos são privados e acessados por métodos, aplicando o encapsulamento da orientação a objetos.

Cada nó tem um array de 26 posições, uma para cada letra de `a` a `z`. As posições apontam para os próximos nós. A raiz é o ponto inicial e não representa uma letra.

### Inserção de palavras

A inserção começa na raiz e segue as letras da palavra. Quando falta um nó no caminho, ele é criado. Ao chegar à última letra, o nó é marcado como fim de palavra.

No código, essa marcação acontece depois do laço de inserção:

```java
atual.marcarFimDaPalavra();
```

Esse método coloca o atributo `fimDaPalavra` como `true`. O marcador é necessário porque um caminho pode existir sem ser uma palavra cadastrada. Se apenas `casa` estiver cadastrada, o caminho `cas` existe, mas não é uma palavra completa no cadastro.

Inserir a mesma palavra novamente reutiliza os nós existentes, sem criar duplicatas. Um nó marcado como fim de palavra também pode ter filhos, como acontece com o último `a` de `casa` quando existe `casaco`.

## Busca, prefixos e autocompletar

### Como o programa sabe qual caminho seguir?

Na busca, o programa recebe a palavra inteira, como em `buscar("casa")`. Ele lê o `c`, depois o `a`, depois o `s` e depois o último `a`.

O programa não testa todas as combinações possíveis. Cada letra é convertida em uma posição do array: `a` corresponde a 0, `b` a 1, `c` a 2, e assim por diante. O acesso à posição é direto.

Estas linhas do auxiliar `encontrarNo` fazem a conversão e o avanço:

```java
int indice = texto.charAt(i) - 'a';
atual = atual.obterFilho(indice);
```

Se alguma ligação não existe, a busca informa que não encontrou a palavra. Se o caminho inteiro existe, o marcador de fim de palavra também é verificado.

### Qual é a diferença entre palavra e prefixo?

A busca exige que o último nó esteja marcado como fim de palavra. A verificação de prefixo exige apenas que o caminho exista.

Se apenas `casa` estiver cadastrada, os resultados serão:

| Consulta | Resultado | Motivo |
| --- | --- | --- |
| `buscar("casa")` | `true` | O caminho existe e tem marcador de fim. |
| `buscar("cas")` | `false` | O caminho existe, mas não tem marcador de fim. |
| `existePrefixo("cas")` | `true` | O caminho existe. |

### Como funciona o autocompletar?

O autocompletar chega primeiro ao nó do prefixo informado. Depois, percorre os ramos abaixo dele para encontrar as palavras cadastradas. Com `casa` e `casaco` na árvore, `autocompletar("cas")` retorna as duas palavras.

O algoritmo conta os resultados antes de criar o array de sugestões. Em seguida, percorre os ramos novamente para preencher esse array. Assim, o array é criado com o tamanho necessário.

Esse percurso usa recursão: o método chama a si mesmo para explorar cada filho. Um `StringBuilder` monta o texto das sugestões enquanto o programa desce pela árvore. Ao voltar de um ramo, a última letra é retirada para explorar o próximo.

Os filhos são visitados de `a` até `z`, então os resultados saem em ordem alfabética. Se o prefixo não existe, o resultado é um array vazio. Se o próprio prefixo também for uma palavra cadastrada, ele aparece nas sugestões.

## Remoção, testes e complexidade

### Remoção de palavras

A remoção primeiro verifica se a palavra está cadastrada. Depois, segue seu caminho e retira o marcador de fim. Na volta do percurso recursivo, são apagados apenas os nós que ficaram sem uso.

Um nó pode ser desligado do pai quando não tem filhos e não marca o fim de outra palavra. Essa regra preserva os caminhos compartilhados.

Por exemplo, ao remover `casa`, seu caminho é mantido se `casaco` ainda estiver cadastrada. Ao remover `casaco`, mantendo `casa`, apenas o trecho final exclusivo de `casaco` é apagado.

O método público de remoção retorna `true` quando remove uma palavra cadastrada e `false` quando ela não existia.

### Testes de funcionamento

O `Main` compara os resultados obtidos com os resultados esperados. Se houver diferença, o programa lança um erro. A mensagem `Todos os testes passaram!` aparece somente se todas as verificações terminarem corretamente.

Os testes cobrem buscas em árvore vazia, palavras existentes e ausentes, prefixos, inserções repetidas, sugestões em ordem alfabética e remoções que devem preservar outras palavras. Também verificam a rejeição de entradas inválidas.

Esta implementação aceita apenas palavras e prefixos não vazios com letras minúsculas de `a` a `z`, sem espaços ou acentos. Essa escolha simplifica o array de filhos de cada nó.

### Qual é a complexidade dos algoritmos?

Big O descreve como o trabalho do algoritmo cresce quando a entrada aumenta. Não representa um tempo exato em segundos.

**L** representa o tamanho da palavra, e **P** representa o tamanho do prefixo.

| Operação | Tempo no pior caso |
| --- | --- |
| Inserir palavra | O(L) |
| Buscar palavra | O(L) |
| Verificar prefixo | O(P) |
| Remover palavra | O(L) |
| Autocompletar | O(P + V + C) |

Inserção, busca e remoção acompanham as letras da palavra. Por isso, seu custo cresce com o tamanho dela. Na remoção, conferir os 26 filhos de cada nó tem custo constante, pois o tamanho do alfabeto não muda.

No autocompletar, **V** é a quantidade de nós visitados na subárvore do prefixo, e **C** é o total de caracteres das palavras retornadas. O custo inclui chegar ao prefixo, explorar os ramos e montar as respostas. Quanto mais sugestões houver, mais trabalho será necessário.

Contar os resultados e depois coletá-los realiza dois percursos da subárvore. Dois percursos de tamanho V continuam sendo O(V), porque o fator constante 2 é desconsiderado na análise Big O. Da mesma forma, validar uma palavra e depois percorrê-la continua sendo O(L).

Em memória, a árvore ocupa **O(N)** para N nós, considerando o alfabeto fixo de 26 letras. Busca e verificação de prefixo usam O(1) de memória auxiliar. A inserção pode criar até O(L) novos nós, e a remoção usa O(L) na pilha de chamadas recursivas.

O autocompletar usa O(P + D + C) de memória, incluindo as respostas. D é a maior quantidade de letras depois do prefixo na subárvore; esse espaço inclui o texto em construção, a pilha recursiva e as palavras retornadas.
