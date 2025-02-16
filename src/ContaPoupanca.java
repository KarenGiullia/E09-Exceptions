import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class ContaPoupanca extends Conta{
    public ContaPoupanca(int numero, double saldo, Cliente dono, double limite) {

        super(numero, saldo, limite, dono);
        setLimite(limite);

    }
    public void setLimite(double limite){
        if (limite>=100 && limite<=1000) {
            this.limite = limite;
            System.out.println("Limite da Conta Corrente permitido!");
        }else {
            this.limite=0;
            System.out.println("Limite da Conta Corrente negado!");
        }
    }

    public double calculaTaxa() {
        return 0;
    }

    @Override
    public void sacar(double valor) {
        double saldoAtual = getSaldo();
        if (valor=>0){
            if (valor <= saldoAtual) {
                double taxa = 0;
                // Verifica se há operações antes de tentar acessar o array
                if (operacaoAtual > 0 && operacoes instanceof OperacaoSaque) {
                    taxa = ((OperacaoSaque) operacoes).calculaTaxa() * valor;
                }

                // Adiciona a taxa de saque ao saldo
                this.setSaldo(saldoAtual - valor - taxa);

                this.operacoes.add(new OperacaoSaque(valor));

                operacaoAtual++; // Incrementa numOp após adicionar a operação
            } else {
                System.out.println("Dinheiro indisponível, valor disponível: R$: " + saldoAtual);
            }
        }else {
            throw new ValorNegativoException(valor);
        }
    }

    @Override
    public void depositar(double valor) {
        double saldoAtual = getSaldo();

        if (valor >= 0) {
            this.setSaldo(saldoAtual + valor);

            this.operacoes.add(new OperacaoDeposito(valor));
            operacaoAtual++;
        }else {
            throw new ValorNegativoException(valor);
        }
    }

    @Override
    public void imprimirExtrato(int tipoOrdenacao) {
        List<Operacao> operacoesOrdenadas = new ArrayList<>(this.operacoes);

        if (tipoOrdenacao == 1) {
            // Ordenar por data de inserção (padrão)
            Collections.sort(operacoesOrdenadas);
        } else if (tipoOrdenacao == 2) {
            // Ordenar por tipo de operação (primeiro depósitos, depois saques)
            Collections.sort(operacoesOrdenadas, new Comparator<Operacao>() {
                @Override
                public int compare(Operacao o1, Operacao o2) {
                    return o1.getTipo().compareTo(o2.getTipo());
                }
            });
        } else {
            System.out.println("Tipo de ordenação inválido.");
            return;
        }

        System.out.println("Extrato da conta de " + this.dono);
        for (Operacao operacao : operacoesOrdenadas) {
            System.out.println(operacao);
        }
    }

}
