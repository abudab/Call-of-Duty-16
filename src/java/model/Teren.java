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
@Table(name = "TEREN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Teren.findAll", query = "SELECT t FROM Teren t"),
    @NamedQuery(name = "Teren.findById", query = "SELECT t FROM Teren t WHERE t.id = :id"),
    @NamedQuery(name = "Teren.findByNazwa", query = "SELECT t FROM Teren t WHERE t.nazwa = :nazwa"),
    @NamedQuery(name = "Teren.findByXres", query = "SELECT t FROM Teren t WHERE t.xres = :xres"),
    @NamedQuery(name = "Teren.findByKolorziemi", query = "SELECT t FROM Teren t WHERE t.kolorziemi = :kolorziemi"),
    @NamedQuery(name = "Teren.findByKolornieba", query = "SELECT t FROM Teren t WHERE t.kolornieba = :kolornieba")})
public class Teren implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAZWA")
    private String nazwa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "XRES")
    private int xres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "KOLORZIEMI")
    private String kolorziemi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "KOLORNIEBA")
    private String kolornieba;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teren")
    private List<Pozycja> pozycjaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teren")
    private List<Igrek> igrekList;

    public Teren() {
    }

    public Teren(Integer id) {
        this.id = id;
    }

    public Teren(Integer id, String nazwa, int xres, String kolorziemi, String kolornieba) {
        this.id = id;
        this.nazwa = nazwa;
        this.xres = xres;
        this.kolorziemi = kolorziemi;
        this.kolornieba = kolornieba;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getXres() {
        return xres;
    }

    public void setXres(int xres) {
        this.xres = xres;
    }

    public String getKolorziemi() {
        return kolorziemi;
    }

    public void setKolorziemi(String kolorziemi) {
        this.kolorziemi = kolorziemi;
    }

    public String getKolornieba() {
        return kolornieba;
    }

    public void setKolornieba(String kolornieba) {
        this.kolornieba = kolornieba;
    }

    @XmlTransient
    public List<Pozycja> getPozycjaList() {
        return pozycjaList;
    }

    public void setPozycjaList(List<Pozycja> pozycjaList) {
        this.pozycjaList = pozycjaList;
    }

    @XmlTransient
    public List<Igrek> getIgrekList() {
        return igrekList;
    }

    public void setIgrekList(List<Igrek> igrekList) {
        this.igrekList = igrekList;
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
        if (!(object instanceof Teren)) {
            return false;
        }
        Teren other = (Teren) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Teren[ id=" + id + " ]";
    }
    
}
