public class Trie {
    private final NoTrie raiz = new NoTrie();

    public void inserir(String palavra) {
        validar(palavra);
        NoTrie atual = raiz;

        for (int i = 0; i < palavra.length(); i++) {
            // 'a' vira 0, 'b' vira 1, ..., 'z' vira 25.
            int indice = palavra.charAt(i) - 'a';

            if (atual.obterFilho(indice) == null) {
                atual.criarFilho(indice);
            }

            atual = atual.obterFilho(indice);
        }

        // O caminho existe; agora indicamos que ele forma uma palavra.
        atual.marcarFimDaPalavra();
    }

    public boolean buscar(String palavra) {
        validar(palavra);
        NoTrie no = encontrarNo(palavra);
        // Um prefixo, como "cas", nao e necessariamente uma palavra.
        return no != null && no.ehFimDaPalavra();
    }

    public boolean existePrefixo(String prefixo) {
        validar(prefixo);
        return encontrarNo(prefixo) != null;
    }

    // Retorna as sugestoes em ordem alfabetica, sem usar colecoes prontas.
    public String[] autocompletar(String prefixo) {
        validar(prefixo);
        NoTrie no = encontrarNo(prefixo);
        if (no == null) {
            return new String[0];
        }

        // Contamos primeiro para criar um array com o tamanho exato.
        String[] palavras = new String[contarPalavras(no)];
        coletarPalavras(no, new StringBuilder(prefixo), palavras, 0);
        return palavras;
    }

    public boolean remover(String palavra) {
        // A busca tambem valida a entrada antes de alterar a arvore.
        if (!buscar(palavra)) {
            return false;
        }
        remover(raiz, palavra, 0);
        return true;
    }

    // Percorre um caminho. Retorna null quando ele nao existe.
    private NoTrie encontrarNo(String texto) {
        NoTrie atual = raiz;

        for (int i = 0; i < texto.length(); i++) {
            int indice = texto.charAt(i) - 'a';
            atual = atual.obterFilho(indice);

            if (atual == null) {
                return null;
            }
        }
        return atual;
    }

    private int contarPalavras(NoTrie no) {
        int total = no.ehFimDaPalavra() ? 1 : 0;
        for (int i = 0; i < 26; i++) {
            if (no.obterFilho(i) != null) {
                total += contarPalavras(no.obterFilho(i));
            }
        }
        return total;
    }

    // A posicao retornada indica onde guardar a proxima palavra no array.
    private int coletarPalavras(NoTrie no, StringBuilder texto, String[] palavras, int posicao) {
        if (no.ehFimDaPalavra()) {
            palavras[posicao] = texto.toString();
            posicao++;
        }

        for (int i = 0; i < 26; i++) {
            NoTrie filho = no.obterFilho(i);
            if (filho != null) {
                texto.append((char) ('a' + i));
                posicao = coletarPalavras(filho, texto, palavras, posicao);
                // Voltamos ao prefixo anterior para explorar o proximo ramo.
                texto.deleteCharAt(texto.length() - 1);
            }
        }
        return posicao;
    }

    // Retorna true se este no ficou sem uso e pode ser removido pelo pai.
    private boolean remover(NoTrie no, String palavra, int posicao) {
        if (posicao == palavra.length()) {
            no.desmarcarFimDaPalavra();
        } else {
            int indice = palavra.charAt(posicao) - 'a';
            if (remover(no.obterFilho(indice), palavra, posicao + 1)) {
                no.removerFilho(indice);
            }
        }
        return !no.ehFimDaPalavra() && !no.temFilhos();
    }

    private void validar(String palavra) {
        if (palavra == null || palavra.isEmpty()) {
            throw new IllegalArgumentException("Informe uma palavra nao vazia.");
        }

        for (int i = 0; i < palavra.length(); i++) {
            char letra = palavra.charAt(i);
            if (letra < 'a' || letra > 'z') {
                throw new IllegalArgumentException("Use apenas letras de a a z, minusculas e sem acentos.");
            }
        }
    }
}
