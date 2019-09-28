package br.com.fredericci.playground;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.h2.tools.Server;

public class Application {

    public static void main(String[] args) throws SQLException {
        startDatabase();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Produto produtoCocaCola = new Produto();
        produtoCocaCola.setNome("Coca-Cola");
        produtoCocaCola.setPreco(1.10);
        entityManager.persist(produtoCocaCola);

        Produto produtoDoritos = new Produto();
        produtoDoritos.setNome("Doritos");
        produtoDoritos.setPreco(3.50);
        entityManager.persist(produtoDoritos);

        Produto produtoMolhoPicante = new Produto();
        produtoMolhoPicante.setNome("Doritos Dippas Molho Picante");
        produtoMolhoPicante.setPreco(2.60);
        entityManager.persist(produtoMolhoPicante);

        Pedido pedido1 = new Pedido();
        pedido1.setStatus(PedidoStatus.EM_PROCESSAMENTO);
        pedido1.setProdutos(Set.of(produtoCocaCola));
        entityManager.persist(pedido1);

        Pedido pedido2 = new Pedido();
        pedido2.setStatus(PedidoStatus.EM_PROCESSAMENTO);
        pedido2.setProdutos(Set.of(produtoCocaCola, produtoDoritos));
        entityManager.persist(pedido2);

        imprimirPedidos(entityManager);

        entityManager.getTransaction().commit();

        System.out.println("Open your browser and navigate to http://localhost:8082/");
        System.out.println("JDBC URL: jdbc:h2:mem:my_database");
        System.out.println("User Name: sa");
        System.out.println("Password: ");
    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }

    private static void imprimirPedidos(EntityManager entityManager) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> query = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> from = query.from(Pedido.class);
        CriteriaQuery<Pedido> select = query.select(from);
        List<Pedido> pedidoList = entityManager.createQuery(select).getResultList();


        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%n %10s %19s %21s %12s %n", "PEDIDO ID", "CRIDADO EM", "STATUS", "TOTAL");
        System.out.println("-----------------------------------------------------------------------------");


        pedidoList.forEach( pedido -> {

                    //Soma o valor de cada produto do pedido
                    Double totalDoPedido = pedido.getProdutos().stream()
                            .map(Produto::getPreco)
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    System.out.printf("%10s, %10tD %<8tT, %20s, %10.2f %n", pedido.getId(), pedido.getCriado(), pedido.getStatus(), totalDoPedido);
                });

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();

    }
}
