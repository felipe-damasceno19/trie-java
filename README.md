# Trie simples em Java

Projeto de estrutura de dados com insercao, busca, verificacao de prefixos, autocompletar e remocao. A trie e seus algoritmos foram implementados do zero, usando objetos e arrays, sem colecoes prontas ou bibliotecas externas. StringBuilder e usado somente para construir o texto das sugestoes.

## Arquivos

- `src/NoTrie.java`: cada no tem um array de 26 filhos e uma indicacao de fim de palavra. Os atributos sao privados.
- `src/Trie.java`: controla a raiz e implementa as cinco operacoes e a validacao das entradas.
- `src/Main.java`: exemplos de uso e testes com verificacao dos resultados.

## Executar no PowerShell

E necessario ter um JDK instalado (com os comandos `javac` e `java`).

```powershell
cd C:\temp\trie-java
javac -d out src/NoTrie.java src/Trie.java src/Main.java
java -cp out Main
```

Se algum resultado estiver errado, o programa lanca um `AssertionError`. Se todos estiverem certos, termina com `Todos os testes passaram!`. Nao e necessario habilitar a opcao `-ea`.

## Exemplo de uso

```java
Trie trie = new Trie();
trie.inserir("casa");
trie.inserir("casaco");
System.out.println(trie.buscar("casa")); // true
System.out.println(trie.buscar("cas"));  // false
System.out.println(trie.buscar("bola")); // false
System.out.println(trie.existePrefixo("cas")); // true

for (String palavra : trie.autocompletar("cas")) {
    System.out.println(palavra); // casa, depois casaco
}

System.out.println(trie.remover("casa")); // true
System.out.println(trie.buscar("casa")); // false
System.out.println(trie.buscar("casaco")); // true
System.out.println(trie.remover("bola")); // false: nao estava cadastrada
```

## Como funciona

O array de cada no tem uma posicao para cada letra: a = 0, b = 1, ..., z = 25. A raiz representa o inicio da arvore e nao representa uma letra.

Ao inserir `casa`, percorremos ou criamos o caminho c -> a -> s -> a e marcamos o ultimo no como fim de palavra. Ao inserir `casaco`, o caminho de `casa` e reutilizado e apenas os nos de c -> o sao criados depois dele.

A busca percorre as letras e retorna verdadeiro somente se chegar a um no marcado como fim de palavra. Assim, `cas` nao e uma palavra cadastrada so porque `casa` existe. Inserir uma palavra repetida reutiliza os mesmos nos.

A verificacao de prefixo apenas confere se o caminho existe, sem exigir um marcador de fim de palavra.

O autocompletar encontra o no do prefixo, conta as palavras abaixo dele para reservar um array e percorre os ramos novamente para preencher esse array. O percurso e recursivo e visita os filhos de a ate z, produzindo ordem alfabetica. A propria palavra do prefixo tambem aparece, se estiver cadastrada. Se o prefixo nao existir, retorna um array vazio.

A remocao primeiro verifica se a palavra existe. Depois percorre seu caminho recursivamente, desmarca o fim da palavra e, na volta, apaga apenas os nos que nao tem filhos e nao marcam outra palavra. Assim, remover `casa` preserva `casaco`, e remover `casaco` preserva `casa`. Retorna verdadeiro quando remove uma palavra e falso quando ela nao estava cadastrada.

## Regras e testes

Sao aceitas somente palavras e prefixos nao vazios com letras minusculas de `a` a `z`, sem espacos ou acentos. Entradas invalidas, inclusive `null`, geram `IllegalArgumentException` em todas as operacoes. A validacao acontece antes da alteracao da arvore. O prefixo vazio nao e aceito nesta versao.

Os testes cobrem arvore vazia, palavras inseridas, prefixos, palavras ausentes, extensoes nao inseridas, insercao repetida, insercao de um prefixo como palavra, palavras de uma letra e entradas invalidas.

Tambem verificam sugestoes ordenadas e sem duplicatas, ausencia de sugestoes, remocao de palavras com caminhos compartilhados, remocao repetida, limpeza de ramos sem uso, esvaziamento da arvore e insercao depois de esvaziar. Os testes comparam os resultados obtidos com os esperados e encerram com erro se houver divergencia.

## Complexidade Big O

Considere L como o numero de caracteres da palavra, P como o tamanho do prefixo, V como o numero de nos da subarvore visitada, D como a maior quantidade de letras depois do prefixo nessa subarvore, C como o total de caracteres das palavras retornadas e N como o total de nos da trie.

| Operacao | Tempo no pior caso | Espaco adicional por chamada |
| --- | --- | --- |
| Inserir | O(L) | O(L), caso seja necessario criar novos nos |
| Buscar | O(L) | O(1) |
| Verificar prefixo | O(P) | O(1) |
| Remover | O(L) | O(L), pela pilha de chamadas recursivas |
| Autocompletar | O(P + V + C) | O(P + D + C), incluindo as palavras retornadas |

A validacao tambem percorre a entrada. Fazer dois percursos de tamanho L continua sendo O(L). O acesso a uma posicao do array custa O(1). Na remocao, verificar os 26 filhos de um no custa O(1), pois o alfabeto e fixo.

O autocompletar percorre a subarvore duas vezes, o que continua sendo O(V), e precisa copiar C caracteres para montar as palavras retornadas. O buffer de texto e a pilha recursiva usam O(P + D) de espaco; o resultado ocupa O(C). Quando o prefixo nao existe, a operacao custa O(P) em tempo e O(1) em espaco adicional.

O espaco total da estrutura e O(26N), equivalente a O(N), pois 26 e constante. Os arrays reservam posicoes mesmo para filhos inexistentes. Inserir uma palavra repetida nao cria novos nos.

Nao ha menu ou interface grafica: o Main demonstra as operacoes por meio dos testes. Os metodos recursivos sao adequados para as palavras curtas usadas neste trabalho; caminhos extremamente longos podem exceder a pilha de chamadas do Java.
