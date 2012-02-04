/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andrzej
 */
@Entity
@Table(name = "POZYCJA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pozycja.findAll", query = "SELECT p FROM Pozycja p"),
    @NamedQuery(name = "Pozycja.findById", query = "SELECT p FROM Pozycja p WHERE p.id = :id"),
    @NamedQuery(name = "Pozycja.findByX", query = "SELECT p FROM Pozycja p WHERE p.x = :x")})
public class Pozycja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "X")
    private int x;
    @JoinColumn(name = "TEREN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Teren teren;

    public Pozycja() {
    }

    public Pozycja(Integer id) {
        this.id = id;
    }

    public Pozycja(Integer id, int x) {
        this.id = id;
        this.x = x;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Teren getTeren() {
        return teren;
    }

    public void setTeren(Teren teren) {
        this.teren = teren;
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
        if (!(object instanceof Pozycja)) {
            return false;
        }
        Pozycja other = (Pozycja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pozycja[ id=" + id + " ]";
    }
    
}
