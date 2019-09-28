package br.com.fredericci.playground;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Pedido {

    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    @CreationTimestamp
    private LocalDateTime criado;

    @ManyToMany
    private Set<Produto> produtos;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(final PedidoStatus status) {
        this.status = status;
    }

    public LocalDateTime getCriado() {
        return criado;
    }

    public void setCriado(final LocalDateTime criado) {
        this.criado = criado;
    }

    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(final Set<Produto> produtos) {
        this.produtos = produtos;
    }
}
