public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();

        verificar("Busca na arvore vazia", false, trie.buscar("casa"));

        trie.inserir("casa");
        trie.inserir("casaco");
        trie.inserir("carro");

        verificar("Palavra casa", true, trie.buscar("casa"));
        verificar("Palavra casaco", true, trie.buscar("casaco"));
        verificar("Palavra carro", true, trie.buscar("carro"));
        verificar("Prefixo cas nao foi inserido", false, trie.buscar("cas"));
        verificar("Palavra bola nao foi inserida", false, trie.buscar("bola"));
        verificar("Extensao casas nao foi inserida", false, trie.buscar("casas"));

        trie.inserir("casa");
        verificar("Insercao repetida de casa", true, trie.buscar("casa"));

        trie.inserir("cas");
        verificar("Prefixo cas agora e uma palavra", true, trie.buscar("cas"));
        verificar("Casa continua existindo", true, trie.buscar("casa"));

        trie.inserir("a");
        trie.inserir("z");
        verificar("Palavra de uma letra: a", true, trie.buscar("a"));
        verificar("Ultima letra do alfabeto: z", true, trie.buscar("z"));

        String[] invalidas = {null, "", "Casa", "casa!", "duas palavras", "a\u00e7ao"};
        for (String palavra : invalidas) {
            verificarEntradaInvalida(trie, palavra);
        }

        testarNovasOperacoes();
        System.out.println("\nTodos os testes passaram!");
    }

    private static void testarNovasOperacoes() {
        Trie trie = new Trie();
        verificar("Prefixo na arvore vazia", false, trie.existePrefixo("ca"));
        verificarPalavras("Autocompletar na arvore vazia", new String[0], trie.autocompletar("ca"));
        verificar("Remover na arvore vazia", false, trie.remover("casa"));

        trie.inserir("carro");
        trie.inserir("casaco");
        trie.inserir("casa");
        trie.inserir("casa");
        trie.inserir("bola");
        verificar("Prefixo compartilhado ca", true, trie.existePrefixo("ca"));
        verificar("Palavra inteira tambem e prefixo", true, trie.existePrefixo("casa"));
        verificar("Prefixo ausente", false, trie.existePrefixo("z"));
        verificarPalavras("Sugestoes ordenadas e sem duplicatas",
                new String[]{"carro", "casa", "casaco"}, trie.autocompletar("ca"));
        verificarPalavras("Inclui a palavra igual ao prefixo",
                new String[]{"casa", "casaco"}, trie.autocompletar("casa"));
        verificarPalavras("Prefixo sem sugestoes", new String[0], trie.autocompletar("z"));
        verificar("Nao remove um prefixo que nao e palavra", false, trie.remover("cas"));
        verificar("Remover palavra inexistente", false, trie.remover("casas"));

        verificar("Remover casa", true, trie.remover("casa"));
        verificar("Casa deixou de ser palavra", false, trie.buscar("casa"));
        verificar("Casaco foi preservado", true, trie.buscar("casaco"));
        verificar("Caminho de casa foi preservado", true, trie.existePrefixo("casa"));
        verificarPalavras("Sugestoes depois da remocao", new String[]{"casaco"}, trie.autocompletar("cas"));
        verificar("Remover a mesma palavra novamente", false, trie.remover("casa"));

        trie.inserir("casa");
        verificar("Remover casaco", true, trie.remover("casaco"));
        verificar("Casa foi preservada", true, trie.buscar("casa"));
        verificar("Ramo exclusivo de casaco foi apagado", false, trie.existePrefixo("casac"));
        verificar("Outro ramo foi preservado", true, trie.buscar("carro"));

        trie.remover("casa");
        trie.remover("carro");
        verificar("Prefixo sem palavras foi apagado", false, trie.existePrefixo("ca"));
        verificar("Bola foi preservada", true, trie.buscar("bola"));
        trie.remover("bola");
        verificarPalavras("Todas as palavras removidas", new String[0], trie.autocompletar("b"));
        trie.inserir("a");
        verificar("Inserir depois de esvaziar", true, trie.buscar("a"));
        verificar("Remover palavra de uma letra", true, trie.remover("a"));
        verificar("Caminho de uma letra apagado", false, trie.existePrefixo("a"));
    }

    private static void verificarPalavras(String descricao, String[] esperadas, String[] obtidas) {
        if (esperadas.length != obtidas.length) {
            throw new AssertionError(descricao + ": quantidade de palavras incorreta");
        }
        for (int i = 0; i < esperadas.length; i++) {
            if (!esperadas[i].equals(obtidas[i])) {
                throw new AssertionError(descricao + ": esperado " + esperadas[i] + ", obtido " + obtidas[i]);
            }
        }
        System.out.print("OK - " + descricao + " -> [");
        for (int i = 0; i < obtidas.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(obtidas[i]);
        }
        System.out.println("]");
    }

    private static void verificar(String descricao, boolean esperado, boolean obtido) {
        if (esperado != obtido) {
            throw new AssertionError(descricao + ": esperado " + esperado + ", obtido " + obtido);
        }
        System.out.println("OK - " + descricao + " -> " + obtido);
    }

    private static void verificarEntradaInvalida(Trie trie, String palavra) {
        try {
            trie.inserir(palavra);
            throw new AssertionError("A insercao deveria rejeitar: " + palavra);
        } catch (IllegalArgumentException esperado) {
        }
        try {
            trie.buscar(palavra);
            throw new AssertionError("A busca deveria rejeitar: " + palavra);
        } catch (IllegalArgumentException esperado) {
        }
        try {
            trie.existePrefixo(palavra);
            throw new AssertionError("O prefixo deveria rejeitar: " + palavra);
        } catch (IllegalArgumentException esperado) {
        }
        try {
            trie.autocompletar(palavra);
            throw new AssertionError("O autocompletar deveria rejeitar: " + palavra);
        } catch (IllegalArgumentException esperado) {
        }
        try {
            trie.remover(palavra);
            throw new AssertionError("A remocao deveria rejeitar: " + palavra);
        } catch (IllegalArgumentException esperado) {
        }
        System.out.println("OK - Entrada invalida rejeitada em todas as operacoes: " + palavra);
    }
}
