/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andrzej
 */
@Entity
@Table(name = "WYNIK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wynik.findAll", query = "SELECT w FROM Wynik w"),
    @NamedQuery(name = "Wynik.findById", query = "SELECT w FROM Wynik w WHERE w.id = :id"),
    @NamedQuery(name = "Wynik.findByNick", query = "SELECT w FROM Wynik w WHERE w.nick = :nick"),
    @NamedQuery(name = "Wynik.findByPunkty", query = "SELECT w FROM Wynik w WHERE w.punkty = :punkty")})
public class Wynik implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NICK")
    private String nick;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNKTY")
    private int punkty;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wynik")
    private List<Highscores> highscoresList;

    public Wynik() {
    }

    public Wynik(Integer id) {
        this.id = id;
    }

    public Wynik(Integer id, String nick, int punkty) {
        this.id = id;
        this.nick = nick;
        this.punkty = punkty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPunkty() {
        return punkty;
    }

    public void setPunkty(int punkty) {
        this.punkty = punkty;
    }

    @XmlTransient
    public List<Highscores> getHighscoresList() {
        return highscoresList;
    }

    public void setHighscoresList(List<Highscores> highscoresList) {
        this.highscoresList = highscoresList;
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
        if (!(object instanceof Wynik)) {
            return false;
        }
        Wynik other = (Wynik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Wynik[ id=" + id + " ]";
    }
    
}
