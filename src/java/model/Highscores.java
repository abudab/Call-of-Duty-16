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
@Table(name = "HIGHSCORES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Highscores.findAll", query = "SELECT h FROM Highscores h"),
    @NamedQuery(name = "Highscores.findById", query = "SELECT h FROM Highscores h WHERE h.id = :id"),
    @NamedQuery(name = "Highscores.findByPozycja", query = "SELECT h FROM Highscores h WHERE h.pozycja = :pozycja")})
public class Highscores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Column(name = "POZYCJA")
    private Integer pozycja;
    @JoinColumn(name = "WYNIK", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Wynik wynik;

    public Highscores() {
    }

    public Highscores(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPozycja() {
        return pozycja;
    }

    public void setPozycja(Integer pozycja) {
        this.pozycja = pozycja;
    }

    public Wynik getWynik() {
        return wynik;
    }

    public void setWynik(Wynik wynik) {
        this.wynik = wynik;
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
        if (!(object instanceof Highscores)) {
            return false;
        }
        Highscores other = (Highscores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Highscores[ id=" + id + " ]";
    }
    
}
