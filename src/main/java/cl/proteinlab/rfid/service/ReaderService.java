package cl.proteinlab.rfid.service;

import com.impinj.octane.ImpinjReader;

/**
 * @author Patricio A. Pérez Valverde
 * @since 23-03-17
 */
public interface ReaderService {

    ImpinjReader startReader();

    ImpinjReader stopReader();

    ImpinjReader startWriter();

}
