package cl.proteinlab.rfid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import cl.proteinlab.rfid.domain.RfidTagLecture;

/**
 * @author Patricio A. Pérez Valverde
 * @since 21-03-17
 */
@Repository
public interface RfidTagLectureRepository extends JpaRepository<RfidTagLecture, Long> {

    List<RfidTagLecture> findByEpcAndFecha(String epc, Date fecha);
}
