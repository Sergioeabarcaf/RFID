package cl.proteinlab.rfid.service;

import cl.proteinlab.rfid.domain.RfidTagLecture;

/**
 * @author Patricio A. Pérez Valverde
 * @since 21-03-17
 */
public interface TagService {

    RfidTagLecture guardar(RfidTagLecture tag);

}
