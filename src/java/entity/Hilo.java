/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Trigi
 */
@Entity
@Table(name = "hilo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hilo.findAll", query = "SELECT h FROM Hilo h")
    , @NamedQuery(name = "Hilo.findByTitulo", query = "SELECT h FROM Hilo h WHERE h.titulo = :titulo")
    , @NamedQuery(name = "Hilo.findById", query = "SELECT h FROM Hilo h WHERE h.id = :id")
    , @NamedQuery(name = "Hilo.findByFecha", query = "SELECT h FROM Hilo h WHERE h.fecha = :fecha")
    , @NamedQuery(name = "Hilo.hilosPorMensajesUsuario", query = "SELECT h FROM Hilo h, Usuario u, Mensaje m WHERE u.email = :id AND m.hilo.id = h.id AND m.usuario.email = u.email")
    , @NamedQuery(name = "Hilo.findByTema", query = "SELECT h from Hilo h WHERE h.tema.titulo = :tema")})
public class Hilo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo")
    private String titulo;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "tema", referencedColumnName = "titulo")
    @ManyToOne(optional = false)
    private Tema tema;
    @JoinColumn(name = "usuario", referencedColumnName = "email")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hilo")
    private Collection<Mensaje> mensajeCollection;

    public Hilo() {
    }

    public Hilo(Integer id) {
        this.id = id;
    }

    public Hilo(Integer id, String titulo, Date fecha) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public Collection<Mensaje> getMensajeCollection() {
        return mensajeCollection;
    }

    public void setMensajeCollection(Collection<Mensaje> mensajeCollection) {
        this.mensajeCollection = mensajeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hilo)) {
            return false;
        }
        Hilo other = (Hilo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Hilo[ id=" + id + " ]";
    }
    
}
