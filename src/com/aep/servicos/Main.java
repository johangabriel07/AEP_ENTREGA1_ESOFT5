package com.aep.servicos;

import com.aep.servicos.model.*;
import com.aep.servicos.service.SolicitacaoService;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static SolicitacaoService service = new SolicitacaoService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Sistema ObservaAção");
            System.out.println("Qual seu tipo de usuário? ");
            System.out.println("1 - Cidadão");
            System.out.println("2 - Atendente / Gestor");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            int perfil = lerInt();

            switch (perfil) {
                case 1 -> menuCidadao();
                case 2 -> menuGestor();
                case 0 -> { System.out.println("Encerrando..."); return; }
                default -> System.out.println("Opção invalida.");
            }
        }
    }


    static void menuCidadao() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\nMENU CIDADÃO\n");
            System.out.println("1 - Registrar solicitação");
            System.out.println("2 - Acompanhar solicitação pelo protocolo: ");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1 -> registrarSolicitacao();
                case 2 -> acompanharSolicitacao();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    static void registrarSolicitacao() {
        System.out.println("\nNova Solicitação\n");

        System.out.print("Informe a descrição do problema: ");
        String desc = sc.nextLine();

        System.out.print("Localização: ");
        String loc = sc.nextLine();


        Categoria categoria = escolherCategoria();
        Prioridade prioridade = escolherPrioridade();

        System.out.print("\nVocê deseja se identificar? (s = sim / n = anônimo): ");
        boolean anonimo = sc.nextLine().trim().equalsIgnoreCase("n");

        String nome = null;
        String contato = null;

        if (!anonimo) {
            System.out.print("Informe seu nome: ");
            nome = sc.nextLine();
            System.out.print("Contato: ");
            contato = sc.nextLine();
        } else {
            System.out.println(" Seus dados pessoais não serão registrados.");
        }

        try {
            String protocolo = service.criar(desc, loc, categoria, prioridade, anonimo, nome, contato);
            Solicitacao s = service.buscar(protocolo);
            System.out.println("\nSolicitação registrada com sucesso!");
            System.out.println("Protocolo: " + protocolo);
            System.out.println("Prazo alvo: " + s.getPrazoAlvo() + " horas");
            System.out.println("Guarde o protocolo para acompanhar sua solicitação.");
        } catch (Exception e) {
            System.out.println("Erro ao registrar");
        }
    }

    static void acompanharSolicitacao() {
        System.out.print("\nInforme o protocolo: ");
        String protocolo = sc.nextLine().trim();
        Solicitacao s = service.buscar(protocolo);

        if (s == null) {
            System.out.println("Solicitação não encontrada.");
            return;
        }

        System.out.println("\n" + s.resumoCompleto());
    }


    static void menuGestor() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\nPainel do Atendente / Gestor");
            System.out.println("1- Listar todas as solicitações");
            System.out.println("2- Filtrar por prioridade");
            System.out.println("3- Filtrar por categoria");
            System.out.println("4- Filtrar por localização");
            System.out.println("5- Ver fila ordenada por prioridade");
            System.out.println("6- Atualizar status de solicitação");
            System.out.println("7  Ver histórico de solicitação");
            System.out.println("0- Voltar");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1 -> listarTodas();
                case 2 -> filtrarPorPrioridade();
                case 3 -> filtrarPorCategoria();
                case 4 -> filtrarPorLocalizacao();
                case 5 -> filaOrdenada();
                case 6 -> atualizarStatus();
                case 7 -> verHistorico();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    static void listarTodas() {
        var lista = service.listar();
        if (lista.isEmpty()) { System.out.println("Nenhuma solicitação registrada..."); return; }
        lista.forEach(s -> System.out.println(s));
    }

    static void filtrarPorPrioridade() {
        Prioridade p = escolherPrioridade();
        var lista = service.listarPorPrioridade(p);
        if (lista.isEmpty()) { System.out.println("Nenhuma solicitação com essa prioridade..."); return; }
        lista.forEach(s -> System.out.println(s));
    }

    static void filtrarPorCategoria() {
        Categoria c = escolherCategoria();
        var lista = service.listarPorCategoria(c);
        if (lista.isEmpty()) { System.out.println("Nenhuma solicitação nessa categoria."); return; }
        lista.forEach(s -> System.out.println(s));
    }

    static void filtrarPorLocalizacao() {
        System.out.print("Localização: ");
        String bairro = sc.nextLine();
        var lista = service.listarPorLocalizacao(bairro);
        if (lista.isEmpty()) { System.out.println("Nenhuma solicitação nesse localização..."); return; }
        lista.forEach(s -> System.out.println(s));
    }

    static void filaOrdenada() {
        var lista = service.filaOrdenada();
        if (lista.isEmpty()) { System.out.println("Fila vazia."); return; }
        System.out.println("\nFila Ordenada por Prioridade");
        lista.forEach(s -> System.out.println(s));
    }

    static void atualizarStatus() {
        System.out.print("\nProtocolo: ");
        String protocolo = sc.nextLine().trim();

        Solicitacao s = service.buscar(protocolo);
        if (s == null) { System.out.println("Solicitação nao encontrada."); return; }

        System.out.println("Status atual: " + s.getStatus());
        System.out.println("Próximo status : " + proximoStatus(s.getStatus()));

        Status novoStatus = escolherStatus();

        System.out.print("Comentário obrigatório: ");
        String comentario = sc.nextLine();

        System.out.print("Seu nome (responsável): ");
        String responsavel = sc.nextLine();

        try {
            service.atualizarStatus(protocolo, novoStatus, comentario, responsavel);
            System.out.println("Status atualizado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    static void verHistorico() {
        System.out.print("\nProtocolo: ");
        Solicitacao s = service.buscar(sc.nextLine().trim());
        if (s == null) { System.out.println("Solicitação não encontrada."); return; }
        System.out.println("\nHistórico");
        s.exibirHistorico();
    }


    static Categoria escolherCategoria() {
        System.out.println("\nCategorias:");
        System.out.println("1- ILUMINACAO");
        System.out.println("2- BURACO");
        System.out.println("3- LIMPEZA");
        System.out.println("4- SAUDE");
        System.out.println("5- ZELADORIA");
        System.out.println("6- SEGURANCA");
        System.out.print("Escolha: ");
        return Categoria.values()[lerInt() - 1];
    }

    static Prioridade escolherPrioridade() {
        System.out.println("\nPrioridade:");
        System.out.println("1 - BAIXA ");
        System.out.println("2 - MEDIA ");
        System.out.println("3 - ALTA ");
        System.out.print("Escolha: ");
        return Prioridade.values()[lerInt() - 1];
    }

    static Status escolherStatus() {
        System.out.println("\nNovo status:");
        System.out.println("1 - TRIAGEM");
        System.out.println("2 - EM_EXECUCAO");
        System.out.println("3 - RESOLVIDO");
        System.out.println("4 - ENCERRADO");
        System.out.print("Escolha: ");
        return Status.values()[lerInt()];
    }

    static String proximoStatus(Status atual) {
        return switch (atual) {
            case ABERTO     -> "TRIAGEM";
            case TRIAGEM    -> "EM_EXECUCAO";
            case EM_EXECUCAO -> "RESOLVIDO";
            case RESOLVIDO  -> "ENCERRADO";
            case ENCERRADO  -> "(encerrado — sem próximo status)";
        };
    }

    static int lerInt() {
        try {
            int v = Integer.parseInt(sc.nextLine().trim());
            return v;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}