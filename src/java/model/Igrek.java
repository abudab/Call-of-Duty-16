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
@Table(name = "IGREK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Igrek.findAll", query = "SELECT i FROM Igrek i"),
    @NamedQuery(name = "Igrek.findById", query = "SELECT i FROM Igrek i WHERE i.id = :id"),
    @NamedQuery(name = "Igrek.findByY", query = "SELECT i FROM Igrek i WHERE i.y = :y")})
public class Igrek implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Y")
    private int y;
    @JoinColumn(name = "TEREN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Teren teren;

    public Igrek() {
    }

    public Igrek(Integer id) {
        this.id = id;
    }

    public Igrek(Integer id, int y) {
        this.id = id;
        this.y = y;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
        if (!(object instanceof Igrek)) {
            return false;
        }
        Igrek other = (Igrek) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Igrek[ id=" + id + " ]";
    }
    
}
