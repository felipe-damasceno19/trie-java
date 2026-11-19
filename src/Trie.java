public class Trie {
    private final NoTrie raiz = new NoTrie();

    public void inserir(String palavra) {
        validar(palavra);
        NoTrie atual = raiz;

        for (int i = 0; i < palavra.length(); i++) {
            int indice = palavra.charAt(i) - 'a';

            if (atual.obterFilho(indice) == null) {
                atual.criarFilho(indice);
            }

            atual = atual.obterFilho(indice);
        }

        atual.marcarFimDaPalavra();
    }

    public boolean buscar(String palavra) {
        validar(palavra);
        NoTrie no = encontrarNo(palavra);
        return no != null && no.ehFimDaPalavra();
    }

    public boolean existePrefixo(String prefixo) {
        validar(prefixo);
        return encontrarNo(prefixo) != null;
    }

    public String[] autocompletar(String prefixo) {
        validar(prefixo);
        NoTrie no = encontrarNo(prefixo);
        if (no == null) {
            return new String[0];
        }

        String[] palavras = new String[contarPalavras(no)];
        coletarPalavras(no, new StringBuilder(prefixo), palavras, 0);
        return palavras;
    }

    public boolean remover(String palavra) {
        if (!buscar(palavra)) {
            return false;
        }
        remover(raiz, palavra, 0);
        return true;
    }

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
                texto.deleteCharAt(texto.length() - 1);
            }
        }
        return posicao;
    }

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
