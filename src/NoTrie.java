// Cada no guarda os caminhos para as proximas letras.
class NoTrie {
    private final NoTrie[] filhos = new NoTrie[26];
    private boolean fimDaPalavra;

    public NoTrie obterFilho(int indice) {
        return filhos[indice];
    }

    public void criarFilho(int indice) {
        filhos[indice] = new NoTrie();
    }

    public void marcarFimDaPalavra() {
        fimDaPalavra = true;
    }

    public boolean ehFimDaPalavra() {
        return fimDaPalavra;
    }

    public void desmarcarFimDaPalavra() {
        fimDaPalavra = false;
    }

    public void removerFilho(int indice) {
        filhos[indice] = null;
    }

    public boolean temFilhos() {
        for (int i = 0; i < filhos.length; i++) {
            if (filhos[i] != null) {
                return true;
            }
        }
        return false;
    }
}
