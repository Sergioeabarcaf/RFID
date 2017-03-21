package cl.proteinlab.rfid.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Patricio A. Pérez Valverde
 * @since 21-03-17
 */
@Entity
@Table(name = "rfid")
public class RfidTagLecture extends BaseBean {
    private static final long serialVersionUID = -145008916864317584L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "epc", nullable = false)
    private String epc;

    @Column(name = "fecha", nullable = false)
    private Date fecha = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RfidTagLecture rfidTag = (RfidTagLecture) o;

        if (id != null ? !id.equals(rfidTag.id) : rfidTag.id != null) return false;
        if (epc != null ? !epc.equals(rfidTag.epc) : rfidTag.epc != null) return false;
        return fecha != null ? fecha.equals(rfidTag.fecha) : rfidTag.fecha == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (epc != null ? epc.hashCode() : 0);
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        return result;
    }
}
